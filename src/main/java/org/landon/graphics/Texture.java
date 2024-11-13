package org.landon.graphics;

import org.landon.project.Project;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class Texture {

    private static final HashMap<String, Texture> loadedTextures = new HashMap<>();

    private int width = 0, height = 0;
    private final File file;
    private int textureId;

    public Texture(String filepath) {
        this.file = new File(filepath);
        if (loadedTextures.containsKey(filepath)) {
            textureId = loadedTextures.get(filepath).getTextureId();
        } else {
            loadTexture();
            loadedTextures.put(filepath, this);
        }
    }

    private void loadTexture() {
        ByteBuffer image;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            image = STBImage.stbi_load(file.getPath(), w, h, comp, 4);
            if (image == null) {
                System.err.println("Couldn't load texture file " + file.getName());
            }

            width = w.get();
            height = h.get();
        }

        textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
        }

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(image);
    }

    public int getTextureId() {
        return textureId;
    }

    public String getTexturePath() {
        return file.getPath();
    }

    public String getLocalPath() {
        return file.getAbsolutePath().replaceAll(Project.getRootDirectory().getAbsolutePath(), "");
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return file.getName();
    }

    public static int getTextureId(String filepath) {
        Texture t = loadedTextures.get(filepath);
        if (t != null) {
            return t.getTextureId();
        } else {
            return -1;
        }
    }

    public static Texture getTexture(String filepath) {
        return loadedTextures.get(filepath);
    }

    public static int loadTexture(String filepath) {
        return new Texture(filepath).getTextureId();
    }

}
