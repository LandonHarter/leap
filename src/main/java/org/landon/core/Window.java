package org.landon.core;

import org.joml.Vector2f;
import org.landon.input.Input;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {

    private static Window instance;

    private int width, height;
    private String title;

    private long window;
    private GLFWWindowSizeCallback windowSize;

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
            }
        };

        GLFW.glfwSetKeyCallback(window, Input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(window, Input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(window, Input.getMouseClickCallback());
        GLFW.glfwSetScrollCallback(window, Input.getMouseScrollCallback());
        GLFW.glfwSetWindowSizeCallback(window, windowSize);
        GLFW.glfwSetWindowSizeCallback(window, windowSize);

        GLFW.glfwShowWindow(window);
        GLFW.glfwSwapInterval(0);

        GL11.glViewport(0, 0, width, height);
    }

    public void startFrame() {
        Time.startFrame();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void endFrame() {
        Input.resetDelta();
        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(window);
        Time.endFrame();
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

    public static Window getInstance() {
        return instance;
    }

}
