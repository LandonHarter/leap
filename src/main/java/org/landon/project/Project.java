package org.landon.project;

import org.landon.scene.Scene;
import org.landon.scene.SceneManager;

import java.io.File;

public class Project {

    private static File rootDirectory;
    private static ProjectConfig config;

    public static void load(File rootDirectory) {
        Project.rootDirectory = rootDirectory;
        Project.config = ProjectConfig.load(rootDirectory);
        ProjectFiles.parseFiles(rootDirectory);
    }

    public static File getRootDirectory() {
        return rootDirectory;
    }

    public ProjectConfig getConfig() {
        return config;
    }

    public static String getName() {
        return config.getName();
    }

    public static void setName(String name) {
        config.setName(name);
        config.save(rootDirectory);
    }

    public static File getLastScene() {
        String lastScenePath = config.getLastScene();
        if (lastScenePath != null) {
            File lastScene = new File(lastScenePath);
            if (lastScene.exists()) {
                return lastScene;
            }
        }

        Scene scene = new Scene("Untitled Scene", false);
        File newScene = new File(rootDirectory, "assets/scenes/scene.leap");
        SceneManager.saveScene(scene, newScene);
        config.save(rootDirectory);
        return newScene;
    }

    public static void setLastScene(String path) {
        config.setLastScene(path);
        config.save(rootDirectory);
    }

}
