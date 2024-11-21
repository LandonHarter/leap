package org.landon.skybox;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBImage;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class SkyboxTexture {

    public static int createSkyboxTexture(File[] textures, SkyboxType type) {
        if (type == SkyboxType.Cubemap) return createCubemap(textures);
        if (type == SkyboxType.HDRI) return createHdri(textures[0]);
        return 0;
    }

    private static int createCubemap(File[] textures) {
        int id = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, id);

        for (int i = 0; i < textures.length; i++) {
            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer channels = BufferUtils.createIntBuffer(1);
            ByteBuffer image = STBImage.stbi_load(textures[i].getAbsolutePath(), width, height, channels, 4);
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, width.get(), height.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
            STBImage.stbi_image_free(image);
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        return id;
    }

    private static int createHdri(File texture) {
        int id = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL30.GL_TEXTURE_2D, id);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        STBImage.stbi_set_flip_vertically_on_load(true);
        FloatBuffer image = STBImage.stbi_loadf(texture.getAbsolutePath(), width, height, channels, 3);
        STBImage.stbi_set_flip_vertically_on_load(false);
        GL11.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL33.GL_RGB16F, width.get(), height.get(), 0, GL11.GL_RGB, GL11.GL_FLOAT, image);
        STBImage.stbi_image_free(image);

        GL11.glTexParameteri(GL30.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL30.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL30.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL33.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL30.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL33.GL_CLAMP_TO_EDGE);

        return id;
    }

}
