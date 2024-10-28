package org.landon.components;

import org.landon.scene.GameObject;

public abstract class Component {

    protected GameObject gameObject;
    private String name;
    private boolean enabled = true;
    private boolean allowMultiple = false;

    public Component(String name, boolean allowMultiple) {
        this.name = name;
        this.allowMultiple = allowMultiple;
    }

    public void start() {}
    public void update() {}
    public void onAdd() {}
    public void onRemove() {}
    public void onComponentAdded(Component component) {}
    public void onComponentRemoved(Component component) {}

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public String getName() {
        return name;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean allowMultiple() {
        return allowMultiple;
    }

}