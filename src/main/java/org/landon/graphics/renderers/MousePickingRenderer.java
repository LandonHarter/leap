package org.landon.graphics.renderers;

import org.joml.Vector3f;
import org.landon.components.graphics.MeshFilter;
import org.landon.components.rendering.MeshRenderer;
import org.landon.graphics.mesh.Mesh;
import org.landon.graphics.shaders.Shader;
import org.landon.math.Transform;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class MousePickingRenderer {

    private Shader shader;

    public MousePickingRenderer() {
        shader = new Shader("resources/shaders/mouse-picking/vert.glsl", "resources/shaders/mouse-picking/frag.glsl");
    }

    public void render(MeshFilter meshFilter) {
        if (meshFilter.getGameObject().getComponent(MeshRenderer.class) == null) return;
        Transform transform = meshFilter.getGameObject().getTransform();
        Mesh mesh = meshFilter.getMesh();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL30.glBindVertexArray(mesh.getVao());
        GL30.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());

        shader.bind();

        shader.setUniform("model", transform.getModelMatrix());
        shader.setUniform("view", SceneManager.getViewMatrix());
        shader.setUniform("projection", SceneManager.getProjectionMatrix());

        int id = SceneManager.getCurrentScene().getObjects().indexOf(meshFilter.getGameObject()) + 1;
        int r = (id & 0x000000FF);
        int g = (id & 0x0000FF00) >> 8;
        int b = (id & 0x00FF0000) >> 16;
        shader.setUniform("id", new Vector3f(r / 255f, g / 255f, b / 255f));

        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);

        shader.unbind();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}
