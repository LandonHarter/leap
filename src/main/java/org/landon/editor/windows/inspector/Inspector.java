package org.landon.editor.windows.inspector;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import org.landon.components.Component;
import org.landon.editor.Icons;
import org.landon.editor.popup.AddComponent;
import org.landon.editor.popup.Popup;
import org.landon.math.Transform;
import org.landon.scene.GameObject;

public class Inspector {

    private static GameObject selectedObject;
    private final static ImString name = new ImString();

    private final static Popup addComponentPopup = new AddComponent();

    public static void render() {
        ImGui.begin("Inspector");
        if (selectedObject == null) {
            ImGui.end();
            return;
        }

        ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
        if (ImGui.checkbox("##enabled", selectedObject.isEnabled())) {
            selectedObject.setEnabled(!selectedObject.isEnabled());
        }
        ImGui.sameLine();
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX() - ImGui.calcTextSize("Name").x);
        if (ImGui.inputText("Name", name)) {
            selectedObject.setName(name.get());
        }
        ImGui.popItemWidth();

        ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
        transform();
        ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);

        for (Component c : selectedObject.getComponents()) {
            ComponentFields.render(c);
            ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
        }

        if (ImGui.button("Add Component", ImGui.getContentRegionAvailX(), 32)) {
            addComponentPopup.open();
        }

        ImGui.end();
    }

    private static void transform() {
        ImGui.image(Icons.getIcon("transform"), 20, 20);
        ImGui.sameLine();
        if (ImGui.treeNodeEx("Transform", ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.SpanAvailWidth)) {
            ImGui.indent(6);
            ImGui.setCursorPosY(ImGui.getCursorPosY() + 3);
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

            ImGui.unindent(6);
            ImGui.treePop();
        }
    }

    public static void setSelectedObject(GameObject object) {
        selectedObject = object;
        if (object != null) name.set(object.getName());
    }

    public static GameObject getSelectedObject() {
        return selectedObject;
    }

}
