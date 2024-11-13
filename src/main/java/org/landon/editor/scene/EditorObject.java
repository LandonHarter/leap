package org.landon.editor.scene;

import org.landon.math.Transform;

public class EditorObject {

    protected Transform transform = new Transform();

    public void update() {}

    public Transform getTransform() {
        return transform;
    }

}
