package org.landon;

import org.landon.components.Camera;
import org.landon.components.graphics.MeshFilter;
import org.landon.components.graphics.MeshRenderer;
import org.landon.core.Window;
import org.landon.graphics.Material;
import org.landon.graphics.Meshes;
import org.landon.graphics.Texture;
import org.landon.scene.Scene;
import org.landon.scene.GameObject;
import org.landon.scene.SceneManager;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(800, 500, "Leap Game Engine");
        window.create();

        Scene scene = new Scene();

        GameObject object = new GameObject();
        object.getTransform().getPosition().set(0, 0, -5);
        object.addComponent(new MeshFilter(Meshes.createSphere(), new Material(new Texture("resources/images/box.jpg"))));
        object.addComponent(new MeshRenderer());
        scene.addObject(object);

        GameObject camera = new GameObject();
        camera.addComponent(new Camera());
        camera.getTransform().getPosition().set(0, 2, 0);
        scene.addObject(camera);

        SceneManager.loadScene(scene);
        while (window.isOpen()) {
            window.startFrame();

            scene.update(); // Renders scene to framebuffer and updates all components

            window.postRender(); // Renders framebuffer
            window.endFrame();
        }
    }
}