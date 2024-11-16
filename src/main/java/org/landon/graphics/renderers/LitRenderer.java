package org.landon.graphics.renderers;

import org.landon.components.graphics.MeshFilter;
import org.landon.components.lighting.Light;
import org.landon.components.rendering.Camera;
import org.landon.editor.windows.logger.Logger;
import org.landon.graphics.material.Material;
import org.landon.graphics.shaders.Shader;
import org.landon.graphics.shaders.ShaderLibrary;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class LitRenderer extends Renderer {

    public LitRenderer() {
        Shader s = new Shader("resources/shaders/lit/vert.glsl", "resources/shaders/lit/frag.glsl", false);
        s.addLibrary(new ShaderLibrary("resources/shaders/libraries/lighting.glsl", ShaderLibrary.ShaderType.FRAGMENT));
        s.createShader();

        shader = s;
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
        Camera camera = SceneManager.getCurrentScene().getCamera();

        shader.setUniform("material.color", material.getColor());
        shader.setUniform("material.shineDamper", material.getShineDamper());
        shader.setUniform("material.reflectivity", material.getReflectivity());
        shader.setUniform("material.tex", 0);

        if (camera != null) shader.setUniform("cameraPosition", camera.getGameObject().getTransform().getWorldPosition());

        for (int i = 0; i < SceneManager.getCurrentScene().getLights().size(); i++) {
            Light light = SceneManager.getCurrentScene().getLights().get(i);
            light.setUniforms(shader, i);
        }
    }

}
