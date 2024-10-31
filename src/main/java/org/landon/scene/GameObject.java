package org.landon.scene;

import org.landon.components.Component;
import org.landon.math.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameObject {

    private transient Scene scene;

    private String uuid;
    private String name;
    private boolean enabled = true;
    private Transform transform;
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

    public void update() {
        if (!enabled) return;
        transform.update(parent != null ? parent.getTransform() : null);
        for (Component component : components) {
            if (component.isEnabled()) {
                component.update();
            }
        }

        for (GameObject child : children) {
            child.update();
        }
    }

    public void destroy() {
        for (Component component : components) {
            component.onRemove();
        }

        components.clear();
        scene = null;
        parent = null;
        transform = null;

        for (GameObject child : children) {
            child.destroy();
        }
        children.clear();
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

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    public List<GameObject> getChildren() {
        return children;
    }

    public void addChild(GameObject child) {
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

}
