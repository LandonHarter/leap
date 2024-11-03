package org.landon.editor.windows;

import imgui.ImColor;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.*;
import org.landon.editor.Icons;
import org.landon.editor.popup.CreateObject;
import org.landon.editor.popup.EditObject;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.scene.GameObject;
import org.landon.scene.SceneManager;

public final class SceneHierarchy {

    private static final CreateObject createObject = new CreateObject();
    private static final EditObject editObject = new EditObject();

    private static final int FRAME_ID = "SceneHierarchy".hashCode();

    public static void render() {
        ImGui.begin("Scene Hierarchy");

        ImGui.pushStyleColor(ImGuiCol.FrameBg, ImGui.getColorU32(ImGuiCol.WindowBg));
        ImGui.beginChildFrame(FRAME_ID, ImGui.getWindowWidth() - 24, ImGui.getWindowHeight() - 50);
        ImGui.popStyleColor();

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
        ImGui.setCursorPosY(ImGui.getCursorPosY() + 5);
        if (header) {
            ImGui.indent();

            for (int i = 0; i < SceneManager.getCurrentScene().getObjects().size(); i++) {
                GameObject obj = SceneManager.getCurrentScene().getObjects().get(i);
                if (obj.getParent() != null) continue;

                renderGameObject(obj);
                ImGui.setCursorPosY(ImGui.getCursorPosY());
                divider(obj);
                ImGui.setCursorPosY(ImGui.getCursorPosY());
            }

            ImGui.unindent();
        }

        if (ImGui.isWindowHovered() && !ImGui.isAnyItemHovered()) {
            if (ImGui.isMouseClicked(0)) {
                Inspector.setSelectedObject(null);
            } else if (ImGui.isMouseClicked(1)) {
                createObject.open();
            }
        }

        ImGui.endChildFrame();
        GameObject grabbed = ImGui.getDragDropPayload(GameObject.class);
        if (grabbed != null && grabbed.getParent() != null) {
            if (ImGui.beginDragDropTarget()) {
                GameObject payload = ImGui.acceptDragDropPayload(GameObject.class);
                if (payload != null) {
                    if (payload.getParent() != null) {
                        payload.getParent().removeChild(payload);
                    }
                }
                ImGui.endDragDropTarget();
            }
        }

        createObject.renderBase();
        editObject.renderBase(Inspector.getSelectedObject());
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
        boolean openEditObject = false;
        if (ImGui.isItemClicked(0)) {
            Inspector.setSelectedObject(obj);
        } else if (ImGui.isItemClicked(1)) {
            Inspector.setSelectedObject(obj);
            openEditObject = true;
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

        if (openEditObject) {
            editObject.open();
        }
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
