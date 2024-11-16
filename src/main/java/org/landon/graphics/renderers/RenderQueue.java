package org.landon.graphics.renderers;

import org.joml.Vector3f;
import org.landon.components.rendering.MeshRenderer;
import org.landon.editor.windows.logger.Logger;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class RenderQueue {

    private static final List<MeshRenderer> opaque = new ArrayList<>();
    private static final List<MeshRenderer> transparent = new ArrayList<>();

    public static void render() {
        for (MeshRenderer renderer : opaque) {
            renderer.render();
        }

        GL11.glDisable(GL11.GL_CULL_FACE);
        for (MeshRenderer renderer : transparent) {
            renderer.render();
        }
        GL11.glEnable(GL11.GL_CULL_FACE);

        opaque.clear();
        transparent.clear();
    }

    public static void addOpaqueRenderer(MeshRenderer renderer) {
        opaque.add(renderer);
    }

    public static void addTransparentRenderer(MeshRenderer renderer) {
        transparent.add(renderer);
    }

}
