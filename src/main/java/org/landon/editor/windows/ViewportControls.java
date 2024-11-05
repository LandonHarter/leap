package org.landon.editor.windows;

import imgui.ImGui;
import org.landon.editor.Editor;
import org.landon.scene.Scene;
import org.landon.scene.SceneManager;
import org.landon.serialization.Serializer;
import org.landon.util.LoadingUtil;

public class ViewportControls {

    private static String originalScene;

    public static void render() {
        ImGui.begin("Viewport Controls");

        if (ImGui.button("Play/Stop")) {
            boolean playing = Editor.isPlaying();
            if (!playing) {
                LoadingUtil.openLoadingScreen("Saving original scene...");
                originalScene = Serializer.toJson(SceneManager.getCurrentScene());
                LoadingUtil.closeLoadingBar();

                Editor.startPlaying();
            }
            if (playing) {
                LoadingUtil.openLoadingScreen("Loading original scene...");
                Scene scene = Serializer.fromJson(originalScene, SceneManager.getCurrentScene().getClass());
                scene.checkForCamera();
                SceneManager.loadScene(scene);
                LoadingUtil.closeLoadingBar();
            }
            Editor.setPlaying(!playing);
        }

        ImGui.end();
    }

}
