package org.landon.graphics.renderers;

import org.landon.components.graphics.MeshFilter;
import org.landon.editor.Editor;
import org.landon.graphics.mesh.Mesh;
import org.landon.graphics.shaders.Shader;
import org.landon.math.Transform;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class WireframeRenderer {

    private static Shader shader;

    public static void init() {
        shader = new Shader("resources/shaders/wireframe/vert.glsl", "resources/shaders/wireframe/frag.glsl");
    }

    public static void render(MeshFilter meshFilter) {
        if (!meshFilter.isInFrustum()) return;
        if (Editor.isPlaying() && SceneManager.getCurrentScene().getCamera() == null) return;
        Transform transform = meshFilter.getGameObject().getTransform();
        Mesh mesh = meshFilter.getMesh();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL30.glBindVertexArray(mesh.getVao());
        GL30.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());

        shader.bind();
        shader.setUniform("model", transform.getModelMatrix());
        shader.setUniform("view", SceneManager.getViewMatrix());
        shader.setUniform("projection", SceneManager.getProjectionMatrix());
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
        shader.unbind();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

}
