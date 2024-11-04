package org.landon.editor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.landon.core.Window;
import org.landon.editor.popup.Popup;
import org.landon.editor.scene.EditorCamera;
import org.landon.editor.windows.ViewportControls;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.editor.windows.SceneHierarchy;
import org.landon.editor.windows.Viewport;

public final class Editor {

    private static boolean playing = false;
    private static final EditorCamera camera = new EditorCamera();

    public static void init() {
        Icons.init();
        Viewport.init();
    }

    public static void render() {
        Dockspace.render();

        SceneHierarchy.render();
        Viewport.render();
        ViewportControls.render();
        Inspector.render();
        Popup.renderPopups();

        ImGui.end();
    }

    public static void setPlaying(boolean playing) {
        Editor.playing = playing;
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static EditorCamera getCamera() {
        return camera;
    }

}
