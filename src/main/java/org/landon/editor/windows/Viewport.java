package org.landon.editor.windows;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import org.landon.core.Window;
import org.landon.editor.Editor;
import org.landon.editor.gizmos.TransformationGizmo;
import org.landon.graphics.framebuffers.Framebuffer;
import org.landon.scene.SceneManager;

public final class Viewport {

    private static Vector2f imagePosition = new Vector2f();
    private static Vector2f imageSize = new Vector2f();
    private static Vector2f position = new Vector2f();

    private static Framebuffer framebuffer;

    public static void init() {
        framebuffer = Window.getInstance().getFramebuffer();
    }

    public static void render() {
        ImGui.begin("Viewport", ImGuiWindowFlags.NoCollapse);

        if (Editor.isPlaying() && SceneManager.getCurrentScene().getCamera() == null) {
            ImVec2 windowSize = new ImVec2();
            ImGui.getContentRegionAvail(windowSize);
            ImGui.setCursorPosX(windowSize.x / 2 - ImGui.calcTextSize("No Camera").x / 2);
            ImGui.setCursorPosY(windowSize.y / 2 - ImGui.calcTextSize("No Camera").y / 2);
            ImGui.text("No Camera");
            ImGui.end();
            return;
        }

        ImVec2 aspectSize = calculateDimensions();
        ImVec2 aspectPosition = calculatePosition(aspectSize);
        ImVec2 windowPosition = ImGui.getWindowPos();

        imagePosition.set(aspectPosition.x, aspectPosition.y);
        imageSize.set(aspectSize.x, aspectSize.y);
        position.set(windowPosition.x, windowPosition.y);

        ImGui.setCursorPos(aspectPosition.x, aspectPosition.y);
        ImGui.image(framebuffer.getTextureID(), aspectSize.x, aspectSize.y, 0, 1, 1, 0);
        Editor.getCamera().movement(ImGui.isWindowHovered());

        TransformationGizmo.update(aspectSize);

        ImGui.end();
    }

    public static Vector2f getImagePosition() {
        return imagePosition;
    }

    public static Vector2f getImageSize() {
        return imageSize;
    }

    public static Vector2f getPosition() {
        return position;
    }

    private static ImVec2 calculateDimensions() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / (16.f / 9.f);
        if (aspectHeight > windowSize.y) {
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * (16.f / 9.f);
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private static ImVec2 calculatePosition(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.f) - (aspectSize.x / 2.f);
        float viewportY = (windowSize.y / 2.f) - (aspectSize.y / 2.f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

}
