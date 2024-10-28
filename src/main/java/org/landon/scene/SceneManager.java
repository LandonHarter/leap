package org.landon.scene;

public final class SceneManager {

    private static Scene currentScene;

    public static void loadScene(Scene scene) {
        if (currentScene != null) {
            currentScene.destroy();
        }
        currentScene = scene;
        currentScene.start();
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

}
