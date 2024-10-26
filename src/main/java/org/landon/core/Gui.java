package org.landon.core;

import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.implot.ImPlot;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import org.landon.input.Input;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL32.*;

public class Gui {

    private static long windowPtr;
    private static final int[] winWidth = new int[1];
    private static final int[] winHeight = new int[1];
    private static final int[] fbWidth = new int[1];
    private static final int[] fbHeight = new int[1];
    private static final double[] mousePosX = new double[1];
    private static final double[] mousePosY = new double[1];
    private static final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static String glslVersion = "#version 330 core";

    public static void initialize(long window) {

        windowPtr = window;

        ImGui.createContext();
        ImNodes.createContext();
        ImPlot.createContext();

        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("EngineAssets/editor.ini");
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors);
        io.setBackendPlatformName("imgui_java_impl_glfw");

        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
        io.setKeyMap(keyMap);

        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

        glfwSetCharCallback(windowPtr, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetScrollCallback(window, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);

            Input.scrollCallback(window, xOffset, yOffset);
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            public void accept(final String s) {
                glfwSetClipboardString(windowPtr, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {

            public String get() {
                final String clipboardString = glfwGetClipboardString(windowPtr);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });

        final ImFontAtlas fontAtlas = io.getFonts();
        fontAtlas.addFontDefault();
        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setMergeMode(true);
        fontConfig.setPixelSnapH(true);
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());
        fontConfig.setMergeMode(false);
        fontConfig.setPixelSnapH(false);
        fontConfig.setRasterizerMultiply(1.2f);
        fontConfig.destroy();

        imGuiGl3.init(glslVersion);
    }

    public static void setupKeyboard(long window, int key, int scancode, int action, int mods) {
        ImGuiIO io = ImGui.getIO();

        if (action == GLFW_PRESS) {
            io.setKeysDown(key, true);
        } else if (action == GLFW_RELEASE) {
            io.setKeysDown(key, false);
        }

        io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
        io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
        io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
        io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
    }

    public static void setupMouse(long window, int button, int action, int mods) {
        ImGuiIO io = ImGui.getIO();

        final boolean[] mouseDown = new boolean[5];

        mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
        mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
        mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
        mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
        mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

        io.setMouseDown(mouseDown);

        if (!io.getWantCaptureMouse() && mouseDown[1]) {
            imgui.ImGui.setWindowFocus(null);
        }
    }

    public static void setupScroll(long window, double offsetx, double offsety) {
        ImGuiIO io = ImGui.getIO();

        io.setMouseWheelH(io.getMouseWheelH() + (float)offsetx);
        io.setMouseWheel(io.getMouseWheel() + (float)offsety);
    }

    public static void startFrame() {
        glClear(GL_COLOR_BUFFER_BIT);

        glfwGetWindowSize(windowPtr, winWidth, winHeight);
        glfwGetFramebufferSize(windowPtr, fbWidth, fbHeight);
        glfwGetCursorPos(windowPtr, mousePosX, mousePosY);

        final ImGuiIO io = imgui.ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale((float) fbWidth[0] / winWidth[0], (float) fbHeight[0] / winHeight[0]);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);

        final int imguiCursor = imgui.ImGui.getMouseCursor();
        glfwSetCursor(windowPtr, mouseCursors[imguiCursor]);
    }

    public static void endFrame() {
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    public static void destroyGui() {
        ImNodes.destroyContext();
        ImPlot.destroyContext(ImPlot.getCurrentContext());

        imGuiGl3.dispose();
        imgui.ImGui.destroyContext();
    }
}