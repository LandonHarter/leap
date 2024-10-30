package org.landon.edtior.windows;

import imgui.ImGui;
import imgui.type.ImString;
import org.landon.scene.GameObject;

public class Inspector {

    private static GameObject selectedObject;
    private static ImString name = new ImString();

    public static void render() {
        ImGui.begin("Inspector");
        if (selectedObject == null) {
            ImGui.end();
            return;
        }

        if (ImGui.checkbox("##enabled", selectedObject.isEnabled())) {
            selectedObject.setEnabled(!selectedObject.isEnabled());
        }
        ImGui.sameLine();
        if (ImGui.inputText("Name", name)) {
            selectedObject.setName(name.get());
        }

        ImGui.end();
    }

    public static void setSelectedObject(GameObject object) {
        selectedObject = object;
        name.set(object.getName());
    }

    public static GameObject getSelectedObject() {
        return selectedObject;
    }

}
