package org.landon.input;

import org.lwjgl.glfw.*;

public final class Input {

    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    private static boolean[] keysPressed = new boolean[GLFW.GLFW_KEY_LAST];
    private static boolean[] keysReleased = new boolean[GLFW.GLFW_KEY_LAST];
    private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static boolean[] buttonsReleased = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static boolean[] buttonsPressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static double mouseX, mouseY;
    private static double mouseDeltaX, mouseDeltaY;
    private static double scrollX, scrollY;

    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public static boolean isKeyPressed(int key) {
        return keysPressed[key];
    }

    public static boolean isKeyReleased(int key) {
        return keysReleased[key];
    }

    public static boolean isButtonDown(int button) {
        return buttons[button];
    }

    public static boolean isButtonPressed(int button) {
        return buttonsPressed[button];
    }

    public static boolean isButtonReleased(int button) {
        return buttonsReleased[button];
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public static double getMouseDeltaX() {
        return mouseDeltaX;
    }

    public static double getMouseDeltaY() {
        return mouseDeltaY;
    }

    public static double getScrollX() {
        return scrollX;
    }

    public static double getScrollY() {
        return scrollY;
    }

    public static GLFWKeyCallback getKeyboardCallback() {
        return new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == -1) return;

                keys[key] = (action != GLFW.GLFW_RELEASE);
                keysReleased[key] = (action == GLFW.GLFW_RELEASE);
                keysPressed[key] = (action == GLFW.GLFW_PRESS);
            }
        };
    }

    public static GLFWCursorPosCallback getMouseMoveCallback() {
        return new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseDeltaX = xpos - mouseX;
                mouseDeltaY = ypos - mouseY;
                mouseX = xpos;
                mouseY = ypos;
            }
        };
    }

    public static GLFWMouseButtonCallback getMouseClickCallback() {
        return new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button == -1) return;

                buttons[button] = (action != GLFW.GLFW_RELEASE);
                buttonsReleased[button] = (action == GLFW.GLFW_RELEASE);
                buttonsPressed[button] = (action == GLFW.GLFW_PRESS);
            }
        };
    }

    public static GLFWScrollCallback getMouseScrollCallback() {
        return new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                scrollX = xoffset;
                scrollY = yoffset;
            }
        };
    }

}
