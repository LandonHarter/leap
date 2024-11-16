package org.landon.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.landon.components.rendering.Camera;
import org.landon.editor.Editor;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.editor.windows.logger.Logger;
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
        scene.loadLights();
        Inspector.setSelectedObject(null);

        if (scene.getFile() != null) {
            Project.setLastScene(scene.getFile().getAbsolutePath());
        }
    }

    public static void saveScene(Scene scene, File f) {
        LoadingUtil.openLoadingScreen("Saving scene file...");

        try {
            String serializedScene = Serializer.toJson(scene);
            FileUtil.writeFile(f, serializedScene);
        } catch (Exception e) {
            Logger.error(e);
        }

        LoadingUtil.closeLoadingBar();
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

    public static Vector3f getCameraPosition() {
        Camera camera = currentScene.getCamera();
        if (!Editor.isPlaying()) {
            return Editor.getCamera().getTransform().getWorldPosition();
        } else {
            if (camera == null) {
                return new Vector3f();
            }
            return camera.getGameObject().getTransform().getWorldPosition();
        }
    }

}
