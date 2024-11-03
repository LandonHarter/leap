package org.landon.editor.popup;

import imgui.ImGui;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.scene.GameObject;
import org.landon.scene.SceneManager;

public class EditObject extends Popup {

    @Override
    public void render(Object... args) {
        GameObject object = (GameObject) args[0];
        if (ImGui.menuItem("Delete")) {
            Inspector.setSelectedObject(null);
            SceneManager.getCurrentScene().removeObject(object);
        }
    }
}
