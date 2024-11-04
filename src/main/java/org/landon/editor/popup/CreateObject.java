package org.landon.editor.popup;

import imgui.ImGui;
import org.landon.components.Camera;
import org.landon.components.graphics.MeshFilter;
import org.landon.components.graphics.MeshRenderer;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.graphics.Meshes;
import org.landon.scene.GameObject;
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
                GameObject obj = new GameObject("Cube");
                obj.addComponent(new MeshFilter(Meshes.createCube()));
                obj.addComponent(new MeshRenderer());
                SceneManager.getCurrentScene().addObject(obj);
                Inspector.setSelectedObject(obj);
            }
            if (ImGui.menuItem("Sphere")) {
                GameObject obj = new GameObject("Sphere");
                obj.addComponent(new MeshFilter(Meshes.createSphere()));
                obj.addComponent(new MeshRenderer());
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
