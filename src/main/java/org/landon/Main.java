package org.landon;

import org.landon.core.Window;
import org.landon.graphics.Material;
import org.landon.graphics.Meshes;
import org.landon.graphics.Texture;
import org.landon.graphics.renderers.Renderer;
import org.landon.scene.Scene;
import org.landon.scene.SceneObject;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(800, 500, "LEAP Physics Engine");
        window.create();

        Scene scene = new Scene();
        SceneObject object = new SceneObject(Meshes.createSphere(), new Material(new Texture("resources/images/box.jpg")));
        object.getTransform().getPosition().set(0, 0, -5);
        scene.addObject(object);

        scene.getCamera().getTransform().getPosition().set(0, 2, 0);

        Renderer renderer = new Renderer();

        while (window.isOpen()) {
            window.startFrame();

            scene.getCamera().update();
            renderer.render(object);

            window.endFrame();
        }
    }
}