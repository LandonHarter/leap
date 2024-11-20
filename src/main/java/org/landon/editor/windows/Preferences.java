package org.landon.editor.windows;

import imgui.ImColor;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiStyleVar;
import org.landon.editor.Editor;

import java.util.HashMap;

public class Preferences extends EditorWindow {

    private final HashMap<String, Runnable> sidebar = new HashMap<>();
    private String selectedSidebarMenu = "General";
    private final int selectedColor = ImColor.rgb("#0b5a71");

    public Preferences() {
        super("Preferences");

        sidebar.put("General", this::general);
        sidebar.put("Editor Camera", this::editorCamera);
    }

    @Override
    public void renderBase() {
        if (open) {
            ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0, 0);
            ImGui.begin(title);
            render();
            ImGui.end();
            ImGui.popStyleVar();
        }
    }

    @Override
    public void render() {
        ImGui.pushStyleColor(ImGuiCol.FrameBg, ImGui.getColorU32(ImGuiCol.WindowBg));
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 8, 10);
        if (ImGui.beginListBox("##Sidebar", 200, ImGui.getWindowSizeY() - 25)) {
            for (String sidebarOption : sidebar.keySet()) {
                boolean selected = sidebarOption.equals(selectedSidebarMenu);
                if (selected) {
                    ImGui.pushStyleColor(ImGuiCol.Header, selectedColor);
                    ImGui.pushStyleColor(ImGuiCol.HeaderHovered, selectedColor);
                }

                ImGui.setNextItemWidth(ImGui.getContentRegionMaxX());
                if (ImGui.selectable(sidebarOption, selected)) {
                    selectedSidebarMenu = sidebarOption;
                }

                if (selected) {
                    ImGui.popStyleColor(2);
                }
            }

            ImGui.endListBox();
        }
        ImGui.popStyleVar();
        ImGui.popStyleColor();
        ImGui.sameLine();
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 10, 10);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, ImGui.getColorU32(ImGuiCol.WindowBg));
        ImGui.beginChildFrame("section".hashCode(), ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY());
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        sidebar.get(selectedSidebarMenu).run();

        if (ImGui.button("Close")) {
            close();
        }

        ImGui.endChildFrame();
    }

    private void general() {
        ImGui.text("General");
    }

    private void editorCamera() {
        float[] moveSpeed = new float[] { Editor.getSettings().getCameraMoveSpeed() };
        if (ImGui.dragFloat("Move Speed", moveSpeed, 0.1f, 0.1f, 1000f)) {
            Editor.getSettings().setCameraMoveSpeed(moveSpeed[0]);
        }

        float[] sensitivity = new float[] { Editor.getSettings().getCameraSensitivity() };
        if (ImGui.dragFloat("Sensitivity", sensitivity, 0.1f, 0.1f, 1000f)) {
            Editor.getSettings().setCameraSensitivity(sensitivity[0]);
        }
    }

}
