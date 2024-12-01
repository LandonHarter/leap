package org.landon.graphics.renderers;

import org.landon.components.graphics.MeshFilter;
import org.landon.graphics.material.Material;
import org.landon.graphics.shaders.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class UnlitRenderer extends Renderer {

    public UnlitRenderer() {
        super(new Shader("resources/shaders/unlit/vert.glsl", "resources/shaders/unlit/frag.glsl"), 2);
    }

    @Override
    public void bindTextures(MeshFilter meshFilter) {
        Material material = meshFilter.getMaterial();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, material.getTexture().getTextureId());
    }

    @Override
    public void unbindTextures() {
        GL13.glActiveTexture(0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public void setUniforms(MeshFilter meshFilter) {
        Material material = meshFilter.getMaterial();
        shader.setUniform("material.tex", 0);
        shader.setUniform("material.color", material.getColor());
    }

}
