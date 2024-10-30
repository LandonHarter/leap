package org.landon.edtior.windows;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import org.landon.math.Transform;
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

        ImGui.image(-1, 20, 20);
        ImGui.sameLine();
        if (ImGui.treeNodeEx("Transform", ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.SpanAvailWidth)) {
            Transform transform = selectedObject.getTransform();

            float[] position = new float[] { transform.getLocalPosition().x, transform.getLocalPosition().y, transform.getLocalPosition().z };
            if (ImGui.dragFloat3("Position", position)) {
                transform.setLocalPosition(position[0], position[1], position[2]);
            }

            float[] rotation = new float[] { transform.getLocalRotation().x, transform.getLocalRotation().y, transform.getLocalRotation().z };
            if (ImGui.dragFloat3("Rotation", rotation)) {
                transform.setLocalRotation(rotation[0], rotation[1], rotation[2]);
            }

            float[] scale = new float[] { transform.getLocalScale().x, transform.getLocalScale().y, transform.getLocalScale().z };
            if (ImGui.dragFloat3("Scale", scale)) {
                transform.setLocalScale(scale[0], scale[1], scale[2]);
            }

            ImGui.treePop();
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
