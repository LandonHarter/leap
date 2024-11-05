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

public class InspectGameObject {

    private static ImString name = new ImString();
    private static Popup addComponentPopup = new AddComponent();

    public static void render(GameObject obj) {
        ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
        if (ImGui.checkbox("##enabled", obj.isEnabled())) {
            obj.setEnabled(!obj.isEnabled());
        }
        ImGui.sameLine();
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX() - ImGui.calcTextSize("Name").x);
        if (ImGui.inputText("Name", name)) {
            obj.setName(name.get());
        }
        ImGui.popItemWidth();

        ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
        transform(obj);
        ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);

        for (Component c : obj.getComponents()) {
            ComponentFields.render(c);
            ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
        }

        if (ImGui.button("Add Component", ImGui.getContentRegionAvailX(), 32)) {
            addComponentPopup.open();
        }
    }

    public static void setName(String newName) {
        name.set(newName);
    }

    private static void transform(GameObject obj) {
        ImGui.image(Icons.getIcon("transform"), 20, 20);
        ImGui.sameLine();
        if (ImGui.treeNodeEx("Transform", ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.SpanAvailWidth)) {
            ImGui.indent(6);
            ImGui.setCursorPosY(ImGui.getCursorPosY() + 3);
            Transform transform = obj.getTransform();

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

}
