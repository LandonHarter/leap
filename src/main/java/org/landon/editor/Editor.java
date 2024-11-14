package org.landon.editor;

import imgui.ImGui;
import org.landon.editor.scene.EditorScene;
import org.landon.editor.scene.Grid;
import org.landon.editor.windows.Profiler;
import org.landon.editor.windows.explorer.ProjectExplorer;
import org.landon.editor.popup.Popup;
import org.landon.editor.scene.EditorCamera;
import org.landon.editor.windows.ViewportControls;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.editor.windows.SceneHierarchy;
import org.landon.editor.windows.Viewport;
import org.landon.scene.SceneManager;

public final class Editor {

    private static boolean playing = false;

    private static final EditorScene scene = new EditorScene();
    private static final EditorCamera camera = new EditorCamera();

    private static EditorSettings settings;

    public static void init() {
        settings = EditorSettings.load();
        camera.setPosition(settings.getCameraPosition()); // must set targetDest as well
        camera.getTransform().setLocalRotation(settings.getCameraRotation());

        Icons.init();
        Viewport.init();
        ProjectExplorer.init();
        Grid.init();
        MousePicking.init();
    }

    public static void render() {
        Dockspace.render();

        SceneHierarchy.render();
        Viewport.render();
        ViewportControls.render();
        Inspector.render();
        ProjectExplorer.render();
        Profiler.render();
        Popup.renderPopups();

        ImGui.end();
    }

    public static void startPlaying() {
        SceneManager.getCurrentScene().start();
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

    public static EditorSettings getSettings() {
        return settings;
    }

    public static EditorScene getScene() {
        return scene;
    }

}
