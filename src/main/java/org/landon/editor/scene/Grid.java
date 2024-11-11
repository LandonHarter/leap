package org.landon.editor.scene;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.landon.editor.Editor;
import org.landon.graphics.Color;
import org.landon.graphics.Mesh;
import org.landon.graphics.shaders.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class Grid {

    private static Shader shader;

    public static float GridScale = 0.5f;
    public static Color GridColor = new Color(0.8f, 0.8f, 0.8f, 1.0f);
    public static Color XAxisColor = new Color(1, 0, 0, 1);
    public static Color ZAxisColor = new Color(0, 0, 1, 1);

    private static Mesh mesh;

    public static void init() {
        shader = new Shader("resources/shaders/grid/vert.glsl", "resources/shaders/grid/frag.glsl");

        Mesh.Vertex[] vertices =  {
                new Mesh.Vertex(new Vector3f(1, 1, 0), new Vector2f(0)),
                new Mesh.Vertex(new Vector3f(-1, -1, 0), new Vector2f(0)),
                new Mesh.Vertex(new Vector3f(-1, 1, 0), new Vector2f(0)),
                new Mesh.Vertex(new Vector3f(1, -1, 0), new Vector2f(0))
        };
        int[] indices = {
                0, 3, 1, 1, 2, 0
        };
        mesh = new Mesh(vertices, indices);
    }

    public static void reset() {
        mesh.destroy();
        shader.destroy();

        init();
    }

    public static void render() {
        if (Editor.isPlaying()) return;

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL30.glBindVertexArray(mesh.getVao());

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());

        shader.bind();
        shader.setUniform("view", Editor.getCamera().getViewMatrix());
        shader.setUniform("projection", Editor.getCamera().getProjection());
        shader.setUniform("gridScale", GridScale);
        shader.setUniform("gridColor", GridColor.getRgb());
        shader.setUniform("xAxisColor", XAxisColor.getRgb());
        shader.setUniform("zAxisColor", ZAxisColor.getRgb());
        shader.setUniform("cameraPosition", Editor.getCamera().getTransform().getLocalPosition());

        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
        shader.unbind();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(true);
    }

}
