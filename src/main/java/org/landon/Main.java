package org.landon;

import org.landon.core.Window;
import org.landon.editor.Editor;
import org.landon.scene.Scene;
import org.landon.scene.SceneManager;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Window window = new Window(1920, 1080, "Leap Game Engine");
        window.create();

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