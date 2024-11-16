package org.landon.editor.scene;

import org.landon.math.Transform;

public class EditorObject {

    protected boolean enabled = true;
    protected Transform transform = new Transform();

    public void update() {}

    public Transform getTransform() {
        return transform;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
