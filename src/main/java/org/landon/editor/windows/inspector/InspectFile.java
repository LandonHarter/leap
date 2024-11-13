package org.landon.editor.windows.inspector;

import imgui.ImGui;
import org.landon.graphics.Texture;
import org.landon.project.ProjectFiles;
import org.landon.util.FileUtil;

import java.io.File;

public class InspectFile {

    public static void render(File file) {
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);

        if (FileUtil.isExtension(file, ProjectFiles.IMAGE_EXTENSIONS)) {
            image(file);
        } else {
            ImGui.text(file.getName());
        }
    }

    private static void image(File file) {
        Texture texture = Texture.getTexture(file.getAbsolutePath());
        if (texture != null) {
            int width = (int) Math.min(ImGui.getContentRegionAvailX(), 350);
            int height = (int) (width * ((float) texture.getHeight() / texture.getWidth()));
            ImGui.image(texture.getTextureId(), width, height);
        }

        ImGui.setCursorPosY(ImGui.getCursorPosY() + 8);

        if (ImGui.collapsingHeader("Properties")) {
            ImGui.indent();
            ImGui.text("Name: " + file.getName());
            ImGui.text("Width: " + texture.getWidth());
            ImGui.text("Height: " + texture.getHeight());
            ImGui.unindent();
        }
    }

}
