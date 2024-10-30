package org.landon.core;

import imgui.ImGui;
import org.landon.graphics.framebuffers.Framebuffer;
import org.landon.graphics.renderers.FramebufferRenderer;
import org.landon.gui.Gui;
import org.landon.input.Input;
import org.landon.serialization.Serializer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Window {

    private static Window instance;

    private int width, height;
    private String title;
    private boolean minimized;

    private long window;
    private GLFWWindowSizeCallback windowSize;

    private Framebuffer framebuffer;
    private FramebufferRenderer framebufferRenderer;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        instance = this;
    }

    public void create() {
        if (!GLFW.glfwInit()) {
            System.err.println("Failed to initialize GLFW");
            return;
        }

        window = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0) {
            System.err.println("Failed to create window");
            return;
        }

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        framebuffer = new Framebuffer(width, height);
        framebufferRenderer = new FramebufferRenderer();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glCullFace(GL11.GL_BACK);

        windowSize = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;

                GL11.glViewport(0, 0, width, height);
                ImGui.getIO().setDisplaySize(width, height);

                framebuffer.resize(width, height);
            }
        };

        GLFW.glfwSetKeyCallback(window, Input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(window, Input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(window, Input.getMouseClickCallback());
        GLFW.glfwSetScrollCallback(window, Input.getMouseScrollCallback());
        GLFW.glfwSetWindowSizeCallback(window, windowSize);
        GLFW.glfwSetWindowSizeCallback(window, windowSize);

        GLFW.glfwShowWindow(window);
        GLFW.glfwSwapInterval(1); // VSync

        IntBuffer iconWidth = BufferUtils.createIntBuffer(1);
        IntBuffer iconHeight = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = STBImage.stbi_load("resources/icons/logo.png", iconWidth, iconHeight, channels, 0);
        GLFWImage icon = GLFWImage.malloc();
        GLFWImage.Buffer iconBuffer = GLFWImage.malloc(1);
        icon.set(iconWidth.get(), iconHeight.get(), image);
        iconBuffer.put(0, icon);

        GLFW.glfwSetWindowIcon(window, iconBuffer);

        GL11.glViewport(0, 0, width, height);
        init();
    }

    private static void init() {
        Serializer.init();
        Gui.init(instance.getWindow());
    }

    public void destroy() {
        windowSize.free();
        framebuffer.destroy();

        Gui.destroy();
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    public void startFrame() {
        framebuffer.bind();
        Time.startFrame();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        Gui.startFrame();
        ImGui.newFrame();
        ImGui.getIO().setFramerate(60);
    }

    public void postRender() {
        framebuffer.unbind();

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        // framebufferRenderer.render(framebuffer);
    }

    public void endFrame() {
        ImGui.render();
        Gui.endFrame();

        Input.resetDelta();
        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(window);
        Time.endFrame();
    }

    public void maximize() {
        GLFW.glfwMaximizeWindow(window);
    }

    public void close() {
        GLFW.glfwSetWindowShouldClose(window, true);
    }

    public boolean isOpen() {
        return !GLFW.glfwWindowShouldClose(window);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public static Window getInstance() {
        return instance;
    }

}
