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
        dockspace();

        SceneHierarchy.render();
        Viewport.render();
        Inspector.render();

        ImGui.end();
    }

    private static void dockspace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
        ImGui.setNextWindowPos(0, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getInstance().getWidth(), Window.getInstance().getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin(" ", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        ImGui.pushStyleColor(ImGuiCol.DockingEmptyBg, 0);
        ImGui.dockSpace(ImGui.getID("dockspace"));
        ImGui.popStyleColor();
    }

}
