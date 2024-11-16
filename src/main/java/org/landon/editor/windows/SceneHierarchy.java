package org.landon.editor.windows;

import imgui.ImColor;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.*;
import org.landon.editor.Icons;
import org.landon.editor.popup.CreateObject;
import org.landon.editor.popup.EditObject;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.editor.windows.logger.Logger;
import org.landon.graphics.mesh.ModelLoader;
import org.landon.scene.GameObject;
import org.landon.scene.SceneManager;
import org.landon.util.DialogUtil;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public final class SceneHierarchy {

    private static final CreateObject createObject = new CreateObject();
    private static final EditObject editObject = new EditObject();

    private static final int FRAME_ID = "SceneHierarchy".hashCode();

    public static void render() {
        ImGui.begin("\uf0e8  Scene Hierarchy");

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

            AtomicInteger index = new AtomicInteger(0);
            float initialY = ImGui.getCursorPosY();
            for (int i = 0; i < SceneManager.getCurrentScene().getObjects().size(); i++) {
                GameObject obj = SceneManager.getCurrentScene().getObjects().get(i);
                if (obj.getParent() != null) continue;

                renderGameObject(obj, index, initialY);
                divider(obj);
            }

            ImGui.unindent();
        }
        if (ImGui.isWindowHovered() && !ImGui.isAnyItemHovered()) {
            if (ImGui.isMouseClicked(0)) {
                Inspector.setSelectedObject(null);
            }
            if (ImGui.isMouseClicked(1)) {
                createObject.open();
            }
        }

        ImGui.endChildFrame();

        if (ImGui.beginDragDropTarget()) {
            GameObject payload = ImGui.acceptDragDropPayload(GameObject.class);
            if (payload != null) {
                if (payload.getParent() != null) {
                    payload.getParent().removeChild(payload);
                }
            }

            File file = ImGui.acceptDragDropPayload(File.class);
            if (file != null) {
                boolean loadTextures = DialogUtil.yesNo("Would you like to load textures?");
                GameObject obj = ModelLoader.loadModel(file.getAbsolutePath(), loadTextures);
                SceneManager.getCurrentScene().addObject(obj);
                Inspector.setSelectedObject(obj);
            }

            ImGui.endDragDropTarget();
        }

        ImGui.end();
    }

    private static final int HeaderColor = ImColor.rgb("#0b5a71");
    private static void renderGameObject(GameObject obj, AtomicInteger index, float initialY) {
        float y = initialY + index.get() * 43;
        ImGui.setCursorPosY(y);
        index.incrementAndGet();

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
        boolean open = ImGui.treeNodeEx(obj.getUuid(), flags, obj.getName());
        if (ImGui.isItemClicked(0)) {
            Inspector.setSelectedObject(obj);
        } else if (ImGui.isItemClicked(1)) {
            editObject.open();
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
                if (payload.getParent() != null) {
                    payload.getParent().removeChild(payload);
                }

                obj.addChild(payload);
            }
            ImGui.endDragDropTarget();
        }

        if (open) {
            for (int i = 0; i < obj.getChildren().size(); i++) {
                renderGameObject(obj.getChildren().get(i), index, initialY);
                divider(obj.getChildren().get(i));
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
