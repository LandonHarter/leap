package org.landon.scene;

import com.alibaba.fastjson.JSON;
import org.joml.Matrix4f;
import org.landon.components.Camera;
import org.landon.editor.Editor;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.project.Project;
import org.landon.serialization.Serializer;
import org.landon.util.FileUtil;
import org.landon.util.LoadingUtil;

import java.io.File;

public final class SceneManager {

    private static Scene currentScene;

    public static void loadScene(Scene scene) {
        if (currentScene != null) {
            currentScene.destroy();
        }
        currentScene = scene;
        Inspector.setSelectedObject(null);

        if (scene.getFile() != null) {
            Project.setLastScene(scene.getFile().getAbsolutePath());
        }
    }

    public static void saveScene(Scene scene, File f) {
        String serializedScene = Serializer.toJson(scene);
        FileUtil.writeFile(f, serializedScene);
    }

    public static Scene readScene(File f) {
        LoadingUtil.openLoadingScreen("Reading scene file...");
        String data = FileUtil.readFile(f);
        Scene s = Serializer.fromJson(data, Scene.class);
        s.setFile(f);
        LoadingUtil.closeLoadingBar();
        return s;
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static Matrix4f getViewMatrix() {
        Camera camera = currentScene.getCamera();
        if (!Editor.isPlaying()) {
            return Editor.getCamera().getViewMatrix();
        } else {
            if (camera == null) {
                return new Matrix4f();
            }
            return camera.getViewMatrix();
        }
    }

    public static Matrix4f getProjectionMatrix() {
        Camera camera = currentScene.getCamera();
        if (!Editor.isPlaying()) {
            return Editor.getCamera().getProjection();
        } else {
            if (camera == null) {
                return new Matrix4f();
            }
            return camera.getProjection();
        }
    }

}
