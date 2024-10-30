package org.landon;

import org.landon.components.Camera;
import org.landon.components.graphics.MeshFilter;
import org.landon.components.graphics.MeshRenderer;
import org.landon.core.Window;
import org.landon.edtior.Editor;
import org.landon.graphics.Material;
import org.landon.graphics.Meshes;
import org.landon.graphics.Texture;
import org.landon.scene.Scene;
import org.landon.scene.GameObject;
import org.landon.scene.SceneManager;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Window window = new Window(1920, 1080, "Leap Game Engine");
        window.create();

        Scene scene = SceneManager.readScene(new File("resources/scene.json"));
        SceneManager.loadScene(scene);

//        Scene scene = new Scene();
//
//        GameObject object = new GameObject("Sphere");
//        object.getTransform().getPosition().set(0, 0, -5);
//        object.addComponent(new MeshFilter(Meshes.createSphere(), new Material(new Texture("resources/images/box.jpg"))));
//        object.addComponent(new MeshRenderer());
//        scene.addObject(object);
//
//        GameObject camera = new GameObject("Camera");
//        camera.addComponent(new Camera());
//        camera.getTransform().getPosition().set(0, 2, 0);
//        scene.addObject(camera);

        scene.start();
        while (window.isOpen()) {
            window.startFrame();

            scene.update(); // Renders scene to framebuffer and updates all components
            window.postRender(); // Unbinds framebuffer

            Editor.render();

            window.endFrame();
        }

        // SceneManager.saveScene(SceneManager.getCurrentScene(), new File("resources/scene.json"));

        window.destroy();
    }

}