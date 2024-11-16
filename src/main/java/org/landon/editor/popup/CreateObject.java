package org.landon.editor.popup;

import imgui.ImGui;
import org.landon.components.rendering.Camera;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.scene.GameObject;
import org.landon.scene.GameObjects;
import org.landon.scene.SceneManager;

public class CreateObject extends Popup {

    @Override
    public void render() {
        if (ImGui.menuItem("Empty Object")) {
            GameObject obj = new GameObject("Empty Object");
            SceneManager.getCurrentScene().addObject(obj);
            Inspector.setSelectedObject(obj);
        }
        if (ImGui.beginMenu("Meshes")) {
            if (ImGui.menuItem("Cube")) {
                GameObject obj = GameObjects.createCube();
                SceneManager.getCurrentScene().addObject(obj);
                Inspector.setSelectedObject(obj);
            }
            if (ImGui.menuItem("Sphere")) {
                GameObject obj = GameObjects.createSphere();
                SceneManager.getCurrentScene().addObject(obj);
                Inspector.setSelectedObject(obj);
            }

            ImGui.endMenu();
        }
        if (ImGui.menuItem("Camera")) {
            GameObject obj = new GameObject("Camera");
            obj.addComponent(new Camera());
            SceneManager.getCurrentScene().addObject(obj);
            Inspector.setSelectedObject(obj);
        }
    }

}
