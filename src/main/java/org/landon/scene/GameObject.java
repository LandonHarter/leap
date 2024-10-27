package org.landon.scene;

import org.landon.components.Component;
import org.landon.math.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private Scene scene;
    private Transform transform;
    private List<Component> components;

    public GameObject() {
        transform = new Transform();
        components = new ArrayList<>();
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
        components.add(component);
        return component;
    }

    public Component getComponent(Class<? extends Component> type) {
        for (Component component : components) {
            if (type.isAssignableFrom(component.getClass())) {
                return component;
            }
        }
        return null;
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

}
