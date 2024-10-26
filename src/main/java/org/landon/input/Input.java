package org.landon.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public final class Input {

    public static GLFWKeyCallback getKeyboardCallback() {
        return new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                System.out.println("Key " + key + " was " + (action == 1 ? "pressed" : "released"));
            }
        };
    }

    public static GLFWCursorPosCallback getMouseMoveCallback() {
        return new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                System.out.println("Mouse moved to (" + xpos + ", " + ypos + ")");
            }
        };
    }

    public static GLFWMouseButtonCallback getMouseClickCallback() {
        return new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                System.out.println("Mouse button " + button + " was " + (action == 1 ? "pressed" : "released"));
            }
        };
    }

    public static GLFWScrollCallback getMouseScrollCallback() {
        return new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                System.out.println("Mouse scrolled by (" + xoffset + ", " + yoffset + ")");
            }
        };
    }

}
