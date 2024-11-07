package org.landon.skybox;

import org.landon.graphics.shaders.Shader;
import org.landon.project.Project;

import java.io.File;

public class Skybox {

    private SkyboxType type;
    private File[] textures;

    private final SkyboxMesh mesh;
    private final Shader shader;

    private int cubemapId = -1;

    public Skybox(SkyboxType type) {
        this.type = type;

        mesh = new SkyboxMesh();
        shader = new Shader("resources/shaders/skybox/vert.glsl", "resources/shaders/skybox/frag.glsl");
    }

    public void createCubemap() {
        cubemapId = SkyboxTexture.createSkyboxTexture(textures, type);
    }

    public void render() {
        if (cubemapId == -1) return;
        mesh.render(this, shader);
    }

    public void setType(SkyboxType type) {
        this.type = type;
    }

    public SkyboxType getType() {
        return type;
    }

    public int getCubemap() {
        return cubemapId;
    }

    public void setTextures(File[] textures) {
        this.textures = textures;
        createCubemap();
    }

    public File[] getTextures() {
        return textures;
    }

    public String[] getTexturePaths() {
        String[] paths = new String[textures.length];
        for (int i = 0; i < textures.length; i++) {
            paths[i] = textures[i].getAbsolutePath().replace(Project.getRootDirectory().getAbsolutePath(), "");
        }
        return paths;
    }

}
