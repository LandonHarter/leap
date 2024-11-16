package org.landon.skybox;

import org.joml.Matrix4f;
import org.landon.graphics.mesh.Mesh;
import org.landon.graphics.mesh.Meshes;
import org.landon.graphics.shaders.Shader;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class SkyboxMesh {

    private Mesh mesh;

    public SkyboxMesh() {
        mesh = Meshes.createSkybox();
    }

    public void render(Skybox skybox, Shader shader) {
        int cubemap = skybox.getCubemap();
        SkyboxType type = skybox.getType();

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL30.glBindVertexArray(mesh.getVao());

        Matrix4f view = new Matrix4f(SceneManager.getViewMatrix());
        view.m30(0);
        view.m31(0);
        view.m32(0);

        GL30.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cubemap);

        GL11.glDepthMask(false);
        shader.bind();

        shader.setUniform("view", view);
        shader.setUniform("projection", SceneManager.getProjectionMatrix());
        shader.setUniform("cubemap", 0);

        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);

        shader.unbind();
        GL11.glDepthMask(true);

        GL13.glActiveTexture(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL13.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
        GL30.glDisableVertexAttribArray(0);

        GL30.glBindVertexArray(0);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

}
