package org.landon.editor.windows.inspector;

import imgui.ImGui;

import java.io.File;

public class InspectFile {

    public static void render(File file) {
        ImGui.text(file.getName());
    }

}
