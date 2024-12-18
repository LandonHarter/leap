package org.landon.editor.scene;

import org.landon.editor.Editor;
import org.landon.editor.windows.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class EditorScene {

    private final List<EditorObject> objects;

    public EditorScene() {
        objects = new ArrayList<>();
    }

    public void update() {
        if (Editor.isPlaying()) return;
        for (EditorObject object : objects) {
            if (object.isEnabled()) object.update();
        }
    }

    public void addObject(EditorObject object) {
        objects.add(object);
    }

    public void removeObject(EditorObject object) {
        objects.remove(object);
    }

}
