package org.landon.graphics.renderers;
import org.landon.components.graphics.MeshFilter;
import org.landon.editor.Editor;
import org.landon.graphics.Material;
import org.landon.graphics.Mesh;
import org.landon.graphics.shaders.Shader;
import org.landon.math.Transform;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public abstract class Renderer {

    protected Shader shader;
    protected int numLayouts = 3;

    public Renderer() {
        shader = new Shader("resources/shaders/unlit/vert.glsl", "resources/shaders/unlit/frag.glsl");
    }

    public Renderer(Shader shader, int numLayouts) {
        this.shader = shader;
        this.numLayouts = numLayouts;
    }

    public void render(MeshFilter meshFilter, boolean outline) {
        if (Editor.isPlaying() && SceneManager.getCurrentScene().getCamera() == null) return;
        Transform transform = meshFilter.getGameObject().getTransform();
        Mesh mesh = meshFilter.getMesh();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL30.glBindVertexArray(mesh.getVao());

        for (int i = 0; i < numLayouts; i++) {
            GL30.glEnableVertexAttribArray(i);
        }

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());
        if (outline) OutlineRenderer.render(meshFilter);

        bindTextures(meshFilter);

        shader.bind();

        shader.setUniform("model", transform.getModelMatrix());
        shader.setUniform("view", SceneManager.getViewMatrix());
        shader.setUniform("projection", SceneManager.getProjectionMatrix());
        setUniforms(meshFilter);

        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);

        shader.unbind();

        GL13.glActiveTexture(0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        for (int i = 0; i < numLayouts; i++) {
            GL30.glDisableVertexAttribArray(i);
        }

        GL30.glBindVertexArray(0);
    }

    public void render(MeshFilter meshFilter) {
        render(meshFilter, false);
    }

    public void bindTextures(MeshFilter meshFilter) {}
    public void setUniforms(MeshFilter meshFilter) {}

}
