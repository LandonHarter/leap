package org.landon;

import org.landon.components.graphics.MeshFilter;
import org.landon.components.graphics.MeshRenderer;
import org.landon.core.Window;
import org.landon.graphics.Material;
import org.landon.graphics.Meshes;
import org.landon.graphics.Texture;
import org.landon.graphics.framebuffers.Framebuffer;
import org.landon.graphics.renderers.FramebufferRenderer;
import org.landon.scene.Scene;
import org.landon.scene.GameObject;
import org.lwjgl.opengl.GL11;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(800, 500, "Leap Game Engine");
        window.create();

        Framebuffer framebuffer = new Framebuffer(window.getWidth(), window.getHeight());
        FramebufferRenderer framebufferRenderer = new FramebufferRenderer();

        Scene scene = new Scene();

        GameObject object = new GameObject();
        object.getTransform().getPosition().set(0, 0, -5);
        object.addComponent(new MeshFilter(Meshes.createSphere(), new Material(new Texture("resources/images/box.jpg"))));
        object.addComponent(new MeshRenderer());
        scene.addObject(object);

        scene.getCamera().getTransform().getPosition().set(0, 2, 0);

        while (window.isOpen()) {
            framebuffer.bind();
            window.startFrame();
            scene.update();
            framebuffer.unbind();

            GL11.glClearColor(0, 0, 0, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            framebufferRenderer.render(framebuffer);

            window.endFrame();
        }
    }
}