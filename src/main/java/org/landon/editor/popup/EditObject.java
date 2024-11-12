package org.landon.editor.popup;

import imgui.ImGui;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.scene.GameObject;
import org.landon.scene.SceneManager;

public class EditObject extends Popup {

    @Override
    public void render() {
        if (ImGui.menuItem("Delete")) {
            SceneManager.getCurrentScene().removeObject(Inspector.getSelectedObject());
            Inspector.setSelectedObject(null);
        }
    }
}
