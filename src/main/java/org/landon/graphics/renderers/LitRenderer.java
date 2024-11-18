package org.landon.graphics.renderers;

import org.joml.Vector3f;
import org.landon.components.graphics.MeshFilter;
import org.landon.components.lighting.Light;
import org.landon.components.rendering.Camera;
import org.landon.editor.Editor;
import org.landon.editor.windows.logger.Logger;
import org.landon.graphics.material.Material;
import org.landon.graphics.material.Texture;
import org.landon.graphics.shaders.Shader;
import org.landon.graphics.shaders.ShaderLibrary;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class LitRenderer extends Renderer {

    public LitRenderer() {
        Shader s = new Shader("resources/shaders/lit/vert.glsl", "resources/shaders/lit/geom.glsl", "resources/shaders/lit/frag.glsl", true);

        numLayouts = 5;
        shader = s;
    }

    @Override
    public void bindTextures(MeshFilter meshFilter) {
        Material material = meshFilter.getMaterial();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, material.getTexture().getTextureId());

        Texture normalMap = material.getNormalMap();
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, normalMap != null ? normalMap.getTextureId() : 0);

        Texture specularMap = material.getSpecularMap();
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, specularMap != null ? specularMap.getTextureId() : 0);

        Texture displacementMap = material.getDisplacementMap();
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, displacementMap != null ? displacementMap.getTextureId() : 0);
    }

    @Override
    public void unbindTextures() {
        GL13.glActiveTexture(0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public void setUniforms(MeshFilter meshFilter) {
        Material material = meshFilter.getMaterial();
        Camera camera = SceneManager.getCurrentScene().getCamera();

        shader.setUniform("material.color", material.getColor());
        shader.setUniform("material.shineDamper", material.getShineDamper());
        shader.setUniform("material.reflectivity", material.getReflectivity());
        shader.setUniform("material.metallic", material.getMetallic());
        shader.setUniform("material.alpha", material.getGlossiness() / 200.0f);
        shader.setUniform("material.baseReflectivity", material.getFresnel());

        shader.setUniform("material.tex", 0);

        shader.setUniform("material.hasNormalMap", material.getNormalMap() != null);
        shader.setUniform("material.normalMap", 1);
        shader.setUniform("material.normalMapStrength", material.getNormalMapStrength());

        shader.setUniform("material.hasSpecularMap", material.getSpecularMap() != null);
        shader.setUniform("material.specularMap", 2);
        shader.setUniform("material.specularMapStrength", material.getSpecularMapStrength());

        shader.setUniform("material.hasDisplacementMap", material.getDisplacementMap() != null);
        shader.setUniform("material.displacementMap", 3);
        shader.setUniform("material.displacementMapStrength", material.getDisplacementMapStrength());

        Vector3f cameraPos = new Vector3f(0);
        if (Editor.isPlaying()) {
            cameraPos = Editor.getCamera().getTransform().getWorldPosition();
        } else if (camera != null) {
            cameraPos = camera.getGameObject().getTransform().getWorldPosition();
        }
        shader.setUniform("cameraPosition", cameraPos);

        for (int i = 0; i < SceneManager.getCurrentScene().getLights().size(); i++) {
            Light light = SceneManager.getCurrentScene().getLights().get(i);
            light.setUniforms(shader, i);
        }
    }

}
