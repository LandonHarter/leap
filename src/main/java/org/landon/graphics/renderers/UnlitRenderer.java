package org.landon.graphics.renderers;

import org.landon.components.graphics.MeshFilter;
import org.landon.editor.Editor;
import org.landon.graphics.Material;
import org.landon.graphics.Mesh;
import org.landon.graphics.Shader;
import org.landon.math.Transform;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class UnlitRenderer extends Renderer {

    public UnlitRenderer() {
        super(new Shader("resources/shaders/unlit/vert.glsl", "resources/shaders/unlit/frag.glsl"));
    }

    @Override
    public void render(MeshFilter meshFilter) {
        if (Editor.isPlaying() && SceneManager.getCurrentScene().getCamera() == null) return;
        Transform transform = meshFilter.getGameObject().getTransform();
        Mesh mesh = meshFilter.getMesh();
        Material material = meshFilter.getMaterial();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL30.glBindVertexArray(mesh.getVao());

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());

        if (material.getTexture() != null) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL13.glBindTexture(GL11.GL_TEXTURE_2D, material.getTexture().getTextureId());
        }

        shader.bind();

        shader.setUniform("model", transform.getModelMatrix());
        shader.setUniform("view", SceneManager.getViewMatrix());
        shader.setUniform("projection", SceneManager.getProjectionMatrix());
        shader.setUniform("useTexture", material.getTexture() != null);
        shader.setUniform("color", material.getColor());

        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);

        shader.unbind();

        GL13.glActiveTexture(0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
