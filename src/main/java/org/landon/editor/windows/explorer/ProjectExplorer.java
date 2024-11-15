package org.landon.editor.windows.explorer;

import imgui.ImColor;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiHoveredFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import org.landon.editor.Icons;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.graphics.Texture;
import org.landon.project.Project;
import org.landon.project.ProjectFiles;
import org.landon.util.ThreadUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class ProjectExplorer {

    private static File currentDirectory;
    private static HashMap<String, Integer> fileIcons = new HashMap<>();
    private static final int SELECTED_COLOR = ImColor.rgb("#0b5a71");

    private static boolean canSelect = true;

    public static void init() {
        currentDirectory = Project.getAssetsDirectory();
        assignFileIcons();
    }

    public static void render() {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0, 0);
        ImGui.begin("Project Explorer");
        ImGui.popStyleVar();
        if (ImGui.isWindowHovered() && ImGui.isMouseClicked(0)) {
            Inspector.setSelectedFile(null);
        }

        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 15, 8);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, ImGui.getColorU32(0.15f, 0.15f, 0.15f, 1));
        if (ImGui.beginChildFrame("project-explorer".hashCode(), ImGui.getContentRegionMaxX(), ImGui.calcTextSize("assets").y + 16, ImGuiWindowFlags.NoScrollbar)) {
            String path = currentDirectory.getPath().replace(Project.getRootDirectory().getPath() + "\\", "");
            String[] directories = path.split("\\\\");

            for (String breadcrumb : directories) {
                ImGui.beginGroup();
                ImGui.text(breadcrumb);
                ImGui.sameLine();
                if (!breadcrumb.equals(directories[directories.length - 1])) {
                    ImGui.text(">");
                    ImGui.sameLine();
                }
                ImGui.endGroup();
                ImGui.sameLine();

                if (ImGui.isItemHovered() && ImGui.isMouseClicked(0)) {
                    if (breadcrumb.equals(directories[directories.length - 1])) {
                        currentDirectory = Project.getAssetsDirectory();
                    } else {
                        currentDirectory = new File(Objects.requireNonNull(currentDirectory.getParent()));
                    }
                }
            }
        }
        ImGui.endChildFrame();
        ImGui.popStyleColor();
        ImGui.popStyleVar();
        ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
        ImGui.setCursorPosX(ImGui.getCursorPosX() + 15);

        float availableWidth = ImGui.getContentRegionAvailX();
        float childSize = 96;
        float iconSize = 64;
        float margins = 0;
        int columns = (int) (availableWidth / (iconSize + margins * 2));

        if (currentDirectory != null) {
            File[] directoriesArray = currentDirectory.listFiles(File::isDirectory);
            if (directoriesArray == null) {
                directoriesArray = new File[0];
            }
            List<File> directories = List.of(directoriesArray);

            File[] filesArray = currentDirectory.listFiles(file -> !file.isDirectory());
            if (filesArray == null) {
                filesArray = new File[0];
            }
            List<File> files = List.of(filesArray);

            AtomicInteger i = new AtomicInteger();
            directories.forEach((directory) -> {
                renderFile(directory, childSize, iconSize);
                i.set(checkForNewLine(i.get(), columns, margins));
            });
            files.forEach((file) -> {
                renderFile(file, childSize, iconSize);
                i.set(checkForNewLine(i.get(), columns, margins));
            });
        }

        ImGui.end();
    }

    private static void renderFile(File file, float childSize, float iconSize) {
        String fileName = file.getName();
        boolean isSelected = Inspector.getSelectedFile() != null && Inspector.getSelectedFile().getName().equals(file.getName());
        ImGui.pushStyleColor(ImGuiCol.ChildBg, isSelected ? SELECTED_COLOR : ImGui.getColorU32(ImGuiCol.WindowBg));
        ImGui.pushStyleVar(ImGuiStyleVar.CellPadding, 8, 8);
        ImGui.beginChild(file.getAbsolutePath(), childSize, childSize, false, ImGuiWindowFlags.NoScrollbar);
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        if (file.isFile() && ImGui.beginDragDropSource()) {
            ImGui.setDragDropPayload(file);
            ImGui.image(getIcon(file), 20, 20);
            ImGui.sameLine();
            ImGui.text(file.getName());
            ImGui.endDragDropSource();
        }

        ImGui.setCursorPosX(childSize / 2 - iconSize / 2);
        ImGui.setCursorPosY((childSize - (iconSize + ImGui.getFontSize() + 4)) / 2);
        ImGui.image(getIcon(file), iconSize, iconSize);
        ImGui.setCursorPosX(childSize / 2 - ImGui.calcTextSize(fileName).x / 2);
        ImGui.text(fileName);
        ImGui.endChild();


        if (ImGui.isItemHovered() && ImGui.isMouseReleased(0) && canSelect) {
            Inspector.setSelectedFile(file);
        }
        if (file.isDirectory() && ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(0)) {
            currentDirectory = file;
            Inspector.setSelectedFile(null);

            canSelect = false;
            ThreadUtil.run(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                canSelect = true;
            });
        }
    }

    private static int checkForNewLine(int i, int columns, float margins) {
        if (i < columns - 1) {
            ImGui.sameLine();
            ImGui.dummy(margins, 0);
            ImGui.sameLine();
            i++;
        } else {
            i = 0;
        }

        return i;
    }

    private static void assignFileIcons() {
        fileIcons.put("leap", Icons.getIcon("leap"));
        fileIcons.put("folder", Icons.getIcon("folder"));
    }

    public static int getIcon(File file) {
        String extension = file.isDirectory() ? "folder" : file.getName().substring(file.getName().lastIndexOf(".") + 1);

        for (String imageExtension : ProjectFiles.IMAGE_EXTENSIONS) {
            if (extension.equals(imageExtension)) {
                int textureId = Texture.getTextureId(file.getPath());
                if (textureId != -1) {
                    return textureId;
                }
            }
        }

        return fileIcons.getOrDefault(extension, Icons.getIcon("file"));
    }

}
