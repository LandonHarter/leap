package org.landon.graphics.renderers;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.landon.graphics.Mesh;
import org.landon.graphics.Shader;
import org.landon.graphics.framebuffers.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class FramebufferRenderer {

    private final Shader shader;
    private static final Mesh FRAMEBUFFER_MESH = new Mesh(new Mesh.Vertex[] {
            new Mesh.Vertex(new Vector3f(-1, -1, 0), new Vector2f(0, 0)),
            new Mesh.Vertex(new Vector3f(1, -1, 0), new Vector2f(1, 0)),
            new Mesh.Vertex(new Vector3f(1, 1, 0), new Vector2f(1, 1)),
            new Mesh.Vertex(new Vector3f(-1, 1, 0), new Vector2f(0, 1)),
            new Mesh.Vertex(new Vector3f(-1, -1, 0), new Vector2f(0, 0)),
            new Mesh.Vertex(new Vector3f(1, 1, 0), new Vector2f(1, 1)),
    }, new int[] {
            0, 1, 2,
            2, 3, 0
    });

    public FramebufferRenderer() {
        shader = new Shader("resources/shaders/post-processing/vert.glsl", "resources/shaders/post-processing/frag.glsl");
    }

    public void render(Framebuffer framebuffer) {
        GL30.glBindVertexArray(FRAMEBUFFER_MESH.getVao());

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, FRAMEBUFFER_MESH.getIbo());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.getTextureID());

        shader.bind();
        shader.setUniform("tex", 0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
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
