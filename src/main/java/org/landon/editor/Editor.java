package org.landon.editor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.landon.core.Window;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.editor.windows.SceneHierarchy;
import org.landon.editor.windows.Viewport;

public final class Editor {

    public static void init() {
        Icons.init();
        Viewport.init();
    }

    public static void render() {
        Dockspace.render();

        SceneHierarchy.render();
        Viewport.render();
        Inspector.render();

        ImGui.end();
    }

}
