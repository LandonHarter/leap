package org.landon;

import org.landon.components.graphics.MeshFilter;
import org.landon.components.graphics.MeshRenderer;
import org.landon.core.Window;
import org.landon.graphics.Material;
import org.landon.graphics.Meshes;
import org.landon.graphics.Texture;
import org.landon.scene.Scene;
import org.landon.scene.GameObject;

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

        scene.getCamera().getTransform().getPosition().set(0, 2, 0);

        while (window.isOpen()) {
            window.startFrame();

            scene.update();

            window.endFrame();
        }
    }
}