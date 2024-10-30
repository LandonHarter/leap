package org.landon;

import org.landon.core.Window;
import org.landon.editor.Editor;
import org.landon.project.Project;
import org.landon.scene.Scene;
import org.landon.scene.SceneManager;
import org.landon.util.ExplorerUtil;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        String projectRoot = args.length > 0 ? args[0] : ExplorerUtil.chooseDirectory();
        if (projectRoot == null) {
            System.exit(0);
        }
        Project.load(new File(projectRoot));

        Window window = new Window(1920, 1080, Project.getName() + " | Leap Game Engine");
        window.create();
        window.maximize();

        Scene scene = SceneManager.readScene(new File("resources/scene.json"));
        SceneManager.loadScene(scene);

        scene.start();
        while (window.isOpen()) {
            window.startFrame();

            scene.update(); // Renders scene to framebuffer and updates all components
            window.postRender(); // Unbinds framebuffer

            Editor.render();

            window.endFrame();
        }

        window.destroy();
    }

}