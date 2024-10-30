package org.landon.scene;

import com.alibaba.fastjson.JSON;
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
    }

    public static void saveScene(Scene scene, File f) {
        String serializedScene = Serializer.toJson(scene);
        FileUtil.writeFile(f, serializedScene);
    }

    public static Scene readScene(File f) {
        LoadingUtil.openLoadingScreen("Reading scene file...");
        String data = FileUtil.readFile(f);
        Scene s = Serializer.fromJson(data, Scene.class);
        LoadingUtil.closeLoadingBar();
        return s;
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

}
