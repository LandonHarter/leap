package org.landon.editor.windows.inspector;

import imgui.ImGui;
import org.landon.scene.GameObject;

import java.io.File;

public class Inspector {

    private static GameObject selectedObject;
    private static File selectedFile;

    public static void render() {
        ImGui.begin("Inspector");

        if (selectedObject != null) InspectGameObject.render(selectedObject);
        if (selectedFile != null) InspectFile.render(selectedFile);

        ImGui.end();
    }

    public static void setSelectedObject(GameObject object) {
        selectedObject = object;
        selectedFile = null;
        if (object != null) InspectGameObject.setName(object.getName());
    }

    public static GameObject getSelectedObject() {
        return selectedObject;
    }

    public static void setSelectedFile(File file) {
        selectedFile = file;
        selectedObject = null;
    }

    public static File getSelectedFile() {
        return selectedFile;
    }

}
