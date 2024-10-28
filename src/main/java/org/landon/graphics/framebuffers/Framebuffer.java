package org.landon.graphics.framebuffers;

import org.lwjgl.opengl.GL30C;

public class Framebuffer {

    private int fbo;
    private FrameBufferTexture texture;

    public Framebuffer(int width, int height) {
        generateFramebuffer(width, height);
    }

    public void bind() {
        GL30C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, fbo);
    }

    public void unbind() {
        GL30C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, 0);
    }

    public void destroy() {
        texture.destroy();
        GL30C.glDeleteFramebuffers(fbo);
    }

    public void resize(int width, int height) {
        destroy();
        generateFramebuffer(width, height);
    }

    private void generateFramebuffer(int width, int height) {
        fbo = GL30C.glGenFramebuffers();
        GL30C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, fbo);

        texture = new FrameBufferTexture(width, height);
        GL30C.glFramebufferTexture2D(GL30C.GL_FRAMEBUFFER, GL30C.GL_COLOR_ATTACHMENT0, GL30C.GL_TEXTURE_2D,
                texture.getTextureId(), 0);

        int rbo = GL30C.glGenRenderbuffers();
        GL30C.glBindRenderbuffer(GL30C.GL_RENDERBUFFER, rbo);
        GL30C.glRenderbufferStorage(GL30C.GL_RENDERBUFFER, GL30C.GL_DEPTH_COMPONENT32, width, height);
        GL30C.glFramebufferRenderbuffer(GL30C.GL_FRAMEBUFFER, GL30C.GL_DEPTH_ATTACHMENT, GL30C.GL_RENDERBUFFER, rbo);

        if (GL30C.glCheckFramebufferStatus(GL30C.GL_FRAMEBUFFER) != GL30C.GL_FRAMEBUFFER_COMPLETE) {
            assert false : "Error: Framebuffer is not complete";
        }
        GL30C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, 0);
    }

    public int getFbo() {
        return fbo;
    }

    public int getTextureID() {
        return texture.getTextureId();
    }

}
