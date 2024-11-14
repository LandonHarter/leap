package org.landon.graphics.renderers;

import org.joml.Matrix4f;
import org.landon.components.graphics.MeshFilter;
import org.landon.editor.Editor;
import org.landon.graphics.Mesh;
import org.landon.graphics.shaders.Shader;
import org.landon.math.Transform;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class OutlineRenderer {

    private static Shader shader;

    public static void init() {
        shader = new Shader("resources/shaders/outline/vert.glsl", "resources/shaders/outline/frag.glsl");
    }

    public static void render(MeshFilter meshFilter) {
        if (Editor.isPlaying() && SceneManager.getCurrentScene().getCamera() == null) return;

        shader.bind();

        Matrix4f model = meshFilter.getGameObject().getTransform().getModelMatrix();
        model.scale(1.02f);
        shader.setUniform("model", model);
        shader.setUniform("view", SceneManager.getViewMatrix());
        shader.setUniform("projection", SceneManager.getProjectionMatrix());

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glDrawElements(GL11.GL_TRIANGLES, meshFilter.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

}
