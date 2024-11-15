package org.landon.editor.windows.viewport;

import imgui.ImGui;
import org.landon.editor.Editor;
import org.landon.editor.Icons;
import org.landon.scene.Scene;
import org.landon.scene.SceneManager;
import org.landon.serialization.Serializer;
import org.landon.util.LoadingUtil;

public class ViewportControls {

    private static String originalScene;

    public static void render() {
        ImGui.begin("Viewport Controls");

        ImGui.indent((ImGui.getWindowSizeX() / 2) - 60);
        ImGui.setCursorPosY(ImGui.getWindowSizeY() / 2 - 7.5f);
        if (ImGui.imageButton(Editor.isPlaying() ? Icons.getIcon("nowplaying") : Icons.getIcon("play"), 40, 30)) {
            start();
        }
        ImGui.sameLine();
        if (ImGui.imageButton(Icons.getIcon("stop"), 40, 30)) {
            stop();
        }
        ImGui.unindent((ImGui.getWindowSizeX() / 2) - 60);

        ImGui.end();
    }

    private static void start() {
        if (Editor.isPlaying()) return;

        LoadingUtil.openLoadingScreen("Saving original scene...");
        originalScene = Serializer.toJson(SceneManager.getCurrentScene());
        LoadingUtil.closeLoadingBar();

        Editor.setPlaying(true);
        Editor.startPlaying();
    }

    private static void stop() {
        if (!Editor.isPlaying()) return;

        LoadingUtil.openLoadingScreen("Loading original scene...");
        Scene scene = Serializer.fromJson(originalScene, SceneManager.getCurrentScene().getClass());
        scene.checkForCamera();
        SceneManager.loadScene(scene);
        LoadingUtil.closeLoadingBar();

        Editor.setPlaying(false);
    }

}
