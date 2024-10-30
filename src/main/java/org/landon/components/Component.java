package org.landon.components;

import com.alibaba.fastjson.annotation.JSONField;
import org.landon.scene.GameObject;

import java.util.UUID;

public class Component {

    protected transient GameObject gameObject;

    private final transient String uuid;
    private final transient String name;
    private boolean enabled = true;
    private transient final boolean allowMultiple;

    public Component(String name, boolean allowMultiple) {
        this.uuid = UUID.randomUUID().toString();
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

    public String getUUID() {
        return uuid;
    }

}
