package org.landon.scene;

import com.alibaba.fastjson2.annotation.JSONType;
import org.landon.annoations.RunInEditMode;
import org.landon.components.Component;
import org.landon.editor.Editor;
import org.landon.math.Transform;
import org.landon.serialization.deserializers.GameObjectDeserializer;
import org.landon.serialization.serializers.GameObjectSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JSONType(serializer = GameObjectSerializer.class, deserializer = GameObjectDeserializer.class)
public class GameObject {

    private transient Scene scene;

    private String uuid;
    private String name;
    private boolean enabled = true;

    private Transform transform;
    private transient Transform lastTransform;

    private final List<Component> components;

    private GameObject parent;
    private final List<GameObject> children;

    public GameObject(String name) {
        transform = new Transform();
        components = new ArrayList<>();
        children = new ArrayList<>();
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
    }

    public GameObject() {
        this("Game Object " + (int)(Math.random() * 1000));
    }

    public void start() {
        for (Component component : components) {
            component.start();
        }
    }

    public void editorStart() {
        for (Component component : components) {
            component.editorStart();
        }
    }

    public void update() {
        if (!enabled) return;
        transform.update(parent != null ? parent.getTransform() : null);
        for (Component component : components) {
            if (component.isEnabled()) {
                if (Editor.isPlaying() || component.getClass().isAnnotationPresent(RunInEditMode.class)) {
                    component.update();
                }
            }
        }

        for (GameObject child : children) {
            child.update();
        }

        if (!transform.equals(lastTransform)) {
            for (Component component : components) {
                component.onTransformChange();
            }
            lastTransform = transform.clone();
        }
    }

    public void destroy() {
        for (Component component : components) {
            component.onRemove();
        }

        components.clear();
        for (GameObject child : children) {
            child.destroy();
        }
        children.clear();
    }

    public void onAddToScene() {
        for (Component component : components) {
            component.onAddToScene();
        }
    }

    public void onRemoveFromScene() {
        for (Component component : components) {
            component.onRemoveFromScene();
        }
    }

    public Component addComponent(Component component) {
        if (component.getGameObject() != null) {
            System.err.println("Component already attached to a GameObject");
            return null;
        }

        if (!component.allowMultiple()) {
            Component existingComponent = getComponent(component.getClass());
            if (existingComponent != null) {
                System.err.println("Component already exists on GameObject");
                return null;
            }
        }

        component.setGameObject(this);
        for (Component c : components) {
            c.onComponentAdded(component);
        }
        components.add(component);
        component.onAdd();
        component.createGizmo();
        return component;
    }

    public void removeComponent(Component component) {
        if (component.getGameObject() != this) {
            System.err.println("Component not attached to GameObject");
            return;
        }

        component.onRemove();
        components.remove(component);
        component.setGameObject(null);
        for (Component c : components) {
            c.onComponentRemoved(component);
        }
    }

    public <T extends Component> T getComponent(Class<T> type) {
        for (Component component : components) {
            if (type.isAssignableFrom(component.getClass())) {
                return type.cast(component);
            }
        }
        return null;
    }

    public GameObject getParent() {
        return parent;
    }

    public Transform getParentTransform() {
        if (parent == null) return null;
        return parent.getTransform();
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    public List<GameObject> getChildren() {
        return children;
    }

    public void addChild(GameObject child) {
        GameObject p = child.getParent();
        while (p != null) {
            if (p.getUuid().equals(uuid)) {
                System.err.println("GameObject is already a parent");
                return;
            }
            p = p.getParent();
        }

        children.add(child);
        child.setParent(this);
    }

    public void removeChild(GameObject child) {
        children.remove(child);
        child.setParent(null);
    }

    public List<Component> getComponents() {
        return components;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof GameObject other)) return false;
        return other.getUuid().equals(uuid);
    }
}
