package org.landon.components;

import com.alibaba.fastjson.annotation.JSONField;
import org.landon.editor.scene.EditorObject;
import org.landon.scene.GameObject;

import java.lang.reflect.Field;
import java.util.UUID;

public class Component {

    protected transient GameObject gameObject;

    private final transient String uuid;
    private final transient String name;
    private boolean enabled = true;
    private transient final boolean allowMultiple;
    private transient final boolean canDisable;

    protected transient EditorObject gizmo;

    public Component(String name, boolean allowMultiple, boolean canDisable) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.allowMultiple = allowMultiple;
        this.canDisable = canDisable;
    }

    public void start() {}
    public void update() {}
    public void editorStart() {}
    public void onAdd() {}
    public void onRemove() {}
    public void onAddToScene() {}
    public void onRemoveFromScene() {}
    public void onComponentAdded(Component component) {}
    public void onComponentRemoved(Component component) {}
    public void executeGui(String name) {}
    public void variableUpdated(Field field) {}
    public void createGizmo() {}
    public void onTransformChange() {}

    public Component clone() {
        try {
            return this.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

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

    public boolean canDisable() {
        return canDisable;
    }

    public String getUUID() {
        return uuid;
    }

}
