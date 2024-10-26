package org.landon.graphics.renderers;
import org.landon.graphics.Mesh;
import org.landon.graphics.Shader;
import org.landon.math.Transform;
import org.landon.scene.SceneObject;
import org.lwjgl.opengl.*;

public class Renderer {

    private Shader shader;

    public Renderer() {
        shader = new Shader("resources/shaders/unlit/vert.glsl", "resources/shaders/unlit/frag.glsl");
    }

    public Renderer(Shader shader) {
        this.shader = shader;
    }

    public void render(SceneObject object) {
        Transform transform = object.getTransform();
        Mesh mesh = object.getMesh();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL30.glBindVertexArray(mesh.getVao());

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, object.getMaterial().getTexture().getTextureId());

        shader.bind();

        shader.setUniform("model", transform.getModelMatrix());
        shader.setUniform("view", object.getScene().getCamera().getViewMatrix());
        shader.setUniform("projection", object.getScene().getCamera().getProjection());
        shader.setUniform("color", object.getMaterial().getColor());

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
