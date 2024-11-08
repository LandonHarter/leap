package org.landon.graphics.renderers;

import org.landon.components.graphics.MeshFilter;
import org.landon.graphics.Material;
import org.landon.graphics.shaders.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class LitRenderer extends Renderer {

    public LitRenderer() {
        super(new Shader("resources/shaders/lit/vert.glsl", "resources/shaders/lit/frag.glsl"), 3);
    }

    @Override
    public void bindTextures(MeshFilter meshFilter) {
        Material material = meshFilter.getMaterial();
        if (material.getTexture() != null) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL13.glBindTexture(GL11.GL_TEXTURE_2D, material.getTexture().getTextureId());
        }
    }

    @Override
    public void setUniforms(MeshFilter meshFilter) {
        Material material = meshFilter.getMaterial();
        shader.setUniform("useTexture", material.getTexture() != null);
        shader.setUniform("tex", 0);
        shader.setUniform("color", material.getColor());
    }

}
