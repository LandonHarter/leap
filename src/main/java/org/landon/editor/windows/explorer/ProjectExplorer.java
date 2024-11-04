package org.landon.editor.windows.explorer;

import imgui.ImColor;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import org.landon.editor.Icons;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.project.Project;

import java.io.File;
import java.util.HashMap;

public final class ProjectExplorer {

    private static File currentDirectory;
    private static HashMap<String, Integer> fileIcons = new HashMap<>();
    private static final int SELECTED_COLOR = ImColor.rgb("#0b5a71");

    public static void init() {
        currentDirectory = Project.getRootDirectory();
        assignFileIcons();
    }

    public static void render() {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 20, 20);
        ImGui.begin("Project Explorer");
        ImGui.popStyleVar();

        float availableWidth = ImGui.getContentRegionAvailX();
        float childSize = 96;
        float iconSize = 64;
        float margins = 0;
        int columns = (int) (availableWidth / (iconSize + margins * 2));

        if (currentDirectory != null) {
            int i = 0;
            for (File file : currentDirectory.listFiles()) {
                String fileName = file.getName();
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

                boolean isSelected = Inspector.getSelectedFile() != null && Inspector.getSelectedFile().getName().equals(file.getName());
                ImGui.pushStyleColor(ImGuiCol.ChildBg, isSelected ? SELECTED_COLOR : ImGui.getColorU32(ImGuiCol.WindowBg));
                ImGui.pushStyleVar(ImGuiStyleVar.CellPadding, 8, 8);
                ImGui.beginChild(file.getAbsolutePath(), childSize, childSize, false, ImGuiWindowFlags.NoScrollbar);
                ImGui.popStyleColor();
                ImGui.popStyleVar();

                if (file.isFile() && ImGui.beginDragDropSource()) {
                    ImGui.setDragDropPayload(file);
                    ImGui.image(getIcon(extension), 20, 20);
                    ImGui.sameLine();
                    ImGui.text(file.getName());
                    ImGui.endDragDropSource();
                }

                ImGui.setCursorPosX(childSize / 2 - iconSize / 2);
                ImGui.setCursorPosY((childSize - (iconSize + ImGui.getFontSize() + 4)) / 2);
                ImGui.image(file.isDirectory() ? getIcon("folder") : getIcon(extension), iconSize, iconSize);
                ImGui.setCursorPosX(childSize / 2 - ImGui.calcTextSize(fileName).x / 2);
                ImGui.text(fileName);
                ImGui.endChild();

                if (ImGui.isItemClicked(0)) {
                    Inspector.setSelectedFile(file);
                }
                if (file.isDirectory() && ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(0)) {
                    currentDirectory = file;
                    Inspector.setSelectedFile(null);
                }

                if (i < columns - 1) {
                    ImGui.sameLine();
                    ImGui.dummy(margins, 0);
                    ImGui.sameLine();
                    i++;
                } else {
                    i = 0;
                }
            }
        }

        ImGui.end();
    }

    private static void assignFileIcons() {
        fileIcons.put("leap", Icons.getIcon("leap"));
        fileIcons.put("folder", Icons.getIcon("folder"));
    }

    private static int getIcon(String extension) {
        return fileIcons.getOrDefault(extension, Icons.getIcon("file"));
    }

}
