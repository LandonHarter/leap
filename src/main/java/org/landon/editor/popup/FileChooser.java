package org.landon.editor.popup;

import imgui.ImColor;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiTreeNodeFlags;
import org.landon.editor.windows.explorer.ProjectExplorer;
import org.landon.project.ProjectFiles;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class FileChooser extends Popup {

    private String[] extensions;
    private Consumer<File> onFileSelected;

    private File selectedFile;

    public FileChooser(String[] extensions, Consumer<File> onFileSelected) {
        super();

        this.extensions = extensions;
        this.onFileSelected = onFileSelected;
    }

    @Override
    public void render() {
        ImGui.text("Choose File (." + String.join(", .", extensions) + ")");
        ImGui.beginChildFrame(getId().hashCode(), 300, 400);

        List<File> files = ProjectFiles.getFiles(extensions);
        for (File file : files) {
            float cursorY = ImGui.getCursorPosY();
            ImGui.setCursorPosY(cursorY + 3);
            ImGui.image(ProjectExplorer.getIcon(file), 18, 18);
            ImGui.sameLine();
            ImGui.setCursorPosY(cursorY);

            boolean selected = selectedFile != null && selectedFile.equals(file);
            if (selected) {
                ImGui.pushStyleColor(ImGuiCol.Header, ImColor.rgb("#0b5a71"));
                ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImColor.rgb("#0b5a71"));
            }
            if (ImGui.treeNodeEx(file.getName(), ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.Framed)) {
                if (ImGui.isItemClicked()) {
                    selectedFile = file;
                }

                ImGui.treePop();
            }
            if (selected) ImGui.popStyleColor(2);
        }

        ImGui.endChildFrame();
        if (ImGui.button("Cancel")) {
            selectedFile = null;
            close();
        }
        ImGui.sameLine();
        if (ImGui.button("Select") && selectedFile != null) {
            onFileSelected.accept(selectedFile);
            selectedFile = null;
            close();
        }
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }

    public void setOnFileSelected(Consumer<File> onFileSelected) {
        this.onFileSelected = onFileSelected;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

}
