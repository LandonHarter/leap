package org.landon.editor.windows;

import imgui.ImColor;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiTreeNodeFlags;
import org.landon.editor.Icons;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.scene.GameObject;
import org.landon.scene.SceneManager;

public final class SceneHierarchy {

    public static void render() {
        ImGui.begin("Scene Hierarchy");

        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 4, 8);
        boolean header = ImGui.collapsingHeader("##SceneDropdown", ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.DefaultOpen);
        ImGui.popStyleVar();
        ImGui.sameLine();
        float cursorPosY = ImGui.getCursorPosY();
        ImGui.setCursorPosY(cursorPosY + 7);
        ImGui.image(Icons.getIcon("leap"), 24, 24);
        ImGui.sameLine();
        ImGui.setCursorPosY(cursorPosY);
        ImGui.text(SceneManager.getCurrentScene().getName());
        if (header) {
            ImGui.indent();

            for (int i = 0; i < SceneManager.getCurrentScene().getObjects().size(); i++) {
                GameObject obj = SceneManager.getCurrentScene().getObjects().get(i);
                if (obj.getParent() != null) continue;

                renderGameObject(obj);
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 5);
                divider(obj);
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 5);
            }

            ImGui.unindent();
        }

        if (ImGui.isMouseClicked(0) && ImGui.isWindowHovered() && !ImGui.isAnyItemHovered()) {
            Inspector.setSelectedObject(null);
        }
        ImGui.end();
    }

    private static final int HeaderColor = ImColor.rgb("#0b5a71");
    private static void renderGameObject(GameObject obj) {
        int flags = ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.SpanFullWidth | ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.Framed;
        if (obj.getChildren().isEmpty()) {
            flags |= ImGuiTreeNodeFlags.Leaf;
        }

        boolean selected = obj == Inspector.getSelectedObject();
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 4, 8);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 4);
        if (selected) {
            ImGui.pushStyleColor(ImGuiCol.Header, HeaderColor);
            ImGui.pushStyleColor(ImGuiCol.HeaderHovered, HeaderColor);

            flags |= ImGuiTreeNodeFlags.Selected;
        }

        ImVec2 cursorPos = ImGui.getCursorPos();
        ImGui.setCursorPos(cursorPos.x + 33, cursorPos.y);
        boolean open = ImGui.treeNodeEx(obj.getName(), flags);
        if (ImGui.isItemClicked()) {
            Inspector.setSelectedObject(obj);
        }
        if (selected) {
            ImGui.popStyleColor(2);
        }
        ImGui.popStyleVar(2);

        if (ImGui.beginDragDropSource()) {
            ImGui.setDragDropPayload(obj);
            ImGui.image(Icons.getIcon("gameobject"), 23, 23);
            ImGui.sameLine();

            float cursorY = ImGui.getCursorPosY();
            ImGui.setCursorPosY(cursorY + 2);
            ImGui.text(obj.getName());
            ImGui.setCursorPosY(cursorY);
            ImGui.endDragDropSource();
        }
        if (ImGui.beginDragDropTarget()) {
            GameObject payload = ImGui.acceptDragDropPayload(GameObject.class);
            if (payload != null) {
                obj.addChild(payload);
            }
            ImGui.endDragDropTarget();
        }

        if (open) {
            for (int i = 0; i < obj.getChildren().size(); i++) {
                renderGameObject(obj.getChildren().get(i));
            }

            ImGui.treePop();
        }

        ImGui.setCursorPos(cursorPos.x + 6, cursorPos.y + 6);
        ImGui.image(Icons.getIcon("gameobject"), 23, 23);
    }

    private static void divider(GameObject obj) {
        ImGui.setCursorPosX(0);
        ImGui.pushStyleColor(ImGuiCol.ChildBg, ImGui.getColorU32(ImGuiCol.WindowBg));
        ImGui.beginChild("divider-" + obj.getUuid(), ImGui.getWindowWidth(), 4);
        ImGui.endChild();
        ImGui.popStyleColor();

        if (ImGui.beginDragDropTarget()) {
            GameObject payload = ImGui.acceptDragDropPayload(GameObject.class);
            if (payload != null) {
                SceneManager.getCurrentScene().moveObject(payload, obj);
            }
            ImGui.endDragDropTarget();
        }
    }

}
