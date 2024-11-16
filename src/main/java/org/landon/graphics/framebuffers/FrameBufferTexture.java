package org.landon.graphics.framebuffers;

import org.landon.editor.windows.logger.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL44;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class FrameBufferTexture {
    
    public String filepath;
    public int textureId;
    
    public int width, height;
    
    public FrameBufferTexture(String filepath) {
        try {
            this.filepath = filepath;
            textureId = GL30.glGenTextures();

            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL30.GL_TEXTURE_LOD_BIAS, -0.4f);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer channels = BufferUtils.createIntBuffer(1);
            ByteBuffer image = STBImage.stbi_load(filepath, width, height, channels, 0);

            this.width = width.get(0);
            this.height = height.get(0);

            if (image != null) {
                if (channels.get(0) == 3) {
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width.get(0), height.get(0),
                            0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image);
                } else if (channels.get(0) == 4) {
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width.get(0), height.get(0),
                            0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
                } else {
                    assert false : "Error: (Texture) Unknown number of channels '" + channels.get(0) + "'";
                }
            } else {
                assert false : "Error: (Texture) Could not load image '" + filepath + "'";
            }

            STBImage.stbi_image_free(image);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }

    public FrameBufferTexture(int width, int height) {
        this.filepath = "Generated";

        textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
                0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, 0);
    }

    public FrameBufferTexture(int width, int height, int samples) {
        this.filepath = "Generated";

        textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL44.GL_TEXTURE_2D_MULTISAMPLE, textureId);

        GL11.glTexParameteri(GL44.GL_TEXTURE_2D_MULTISAMPLE, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL44.GL_TEXTURE_2D_MULTISAMPLE, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL44.glTexImage2DMultisample(GL44.GL_TEXTURE_2D_MULTISAMPLE, samples, GL11.GL_RGB, width, height, true);
        GL11.glBindTexture(GL44.GL_TEXTURE_2D_MULTISAMPLE, 0);
    }

    public void destroy() {
        GL11.glDeleteTextures(textureId);
    }
    
    public ByteBuffer getBuffer() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load(filepath, w, h, comp, 4);
            if (image == null) {
                System.err.println("Couldn't load " + filepath);
            }

            return image;
        }
        catch (Exception e) {
            Logger.error(e);
        }

        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureId() {
        return textureId;
    }

}