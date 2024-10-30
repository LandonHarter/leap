package org.landon.scene;

import org.landon.components.Component;
import org.landon.math.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private transient Scene scene;

    private final transient String name;
    private Transform transform;
    private final List<Component> components;

    public GameObject(String name) {
        transform = new Transform();
        components = new ArrayList<>();
        this.name = name;
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
        for (Component component : components) {
            if (component.isEnabled()) {
                component.update();
            }
        }
    }

    public void destroy() {
        for (Component component : components) {
            component.onRemove();
        }

        components.clear();
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

    public String getName() {
        return name;
    }

}
