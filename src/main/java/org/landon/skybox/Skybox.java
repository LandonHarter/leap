package org.landon.skybox;

import org.landon.graphics.shaders.Shader;
import org.landon.serialization.types.LeapEnum;
import org.landon.serialization.types.LeapFile;
import org.landon.project.Project;

import java.io.File;

public class Skybox {

    private final LeapEnum<SkyboxType> type = new LeapEnum<>(SkyboxType.Cubemap);
    private LeapFile[] textures;

    private final SkyboxMesh mesh;
    private final Shader cubemapShader;
    private final Shader hdriShader;

    private int texture = -1;

    public Skybox(SkyboxType type) {
        this.type.setValue(type);

        mesh = new SkyboxMesh();
        cubemapShader = new Shader("resources/shaders/skybox/cubemap/vert.glsl", "resources/shaders/skybox/cubemap/frag.glsl");
        hdriShader = new Shader("resources/shaders/skybox/hdri/vert.glsl", "resources/shaders/skybox/hdri/frag.glsl");
    }

    public void createCubemap() {
        texture = SkyboxTexture.createSkyboxTexture(textures, type.getValue());
    }

    public void render() {
        if (texture == -1) return;
        mesh.render(this, type.getValue() == SkyboxType.Cubemap ? cubemapShader : hdriShader);
    }

    public void setType(SkyboxType type) {
        this.type.setValue(type);
    }

    public SkyboxType getType() {
        return type.getValue();
    }

    public int getTexture() {
        return texture;
    }

    public void setTextures(File[] textures) {
        this.textures = new LeapFile[textures.length];
        for (int i = 0; i < textures.length; i++) {
            this.textures[i] = new LeapFile(textures[i]);
        }
        createCubemap();
    }

    public void setTextures(LeapFile[] textures) {
        this.textures = textures;
        createCubemap();
    }

    public LeapFile[] getTextures() {
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
