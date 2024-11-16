package org.landon.editor;

import org.joml.Vector2f;
import org.landon.components.graphics.MeshFilter;
import org.landon.components.rendering.MeshRenderer;
import org.landon.core.Window;
import org.landon.editor.windows.viewport.Viewport;
import org.landon.graphics.framebuffers.Framebuffer;
import org.landon.graphics.renderers.MousePickingRenderer;
import org.landon.input.Input;
import org.landon.scene.GameObject;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class MousePicking {

    private static Framebuffer framebuffer;
    private static MousePickingRenderer renderer;

    private static boolean rendered = false;

    public static void init() {
        framebuffer = new Framebuffer(Window.getInstance().getWidth(), Window.getInstance().getHeight());
        renderer = new MousePickingRenderer();
    }

    public static GameObject click() {
        framebuffer.bind();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(1, 1, 1, 1);
        GL11.glViewport(0, 0, Window.getInstance().getWidth(), Window.getInstance().getHeight());

        List<MeshFilter> meshFilters = new ArrayList<>();
        for (GameObject gameObject : SceneManager.getCurrentScene().getObjects()) {
            MeshFilter meshFilter = gameObject.getComponent(MeshFilter.class);
            MeshRenderer meshRenderer = gameObject.getComponent(MeshRenderer.class);

            if (meshFilter != null && meshRenderer != null) {
                meshFilters.add(meshFilter);
            }
        }

        for (MeshFilter meshFilter : meshFilters) {
            renderer.render(meshFilter);
        }

        Vector2f mouse = Input.getMousePosition();
        Vector2f viewportPosition = Viewport.getPosition(), imageSize = Viewport.getImageSize(), imagePosition = Viewport.getImagePosition();
        float x = inverseLerp(viewportPosition.x + imagePosition.x, viewportPosition.x + imagePosition.x + imageSize.x, 0, 1, mouse.x);
        float y = inverseLerp(viewportPosition.y + imagePosition.y, viewportPosition.y + imagePosition.y + imageSize.y, 1, 0, mouse.y);

        if (x < 0 || x > 1 || y < 0 || y > 1) {
            framebuffer.unbind();
            return null;
        }

        x *= Window.getInstance().getWidth();
        y *= Window.getInstance().getHeight();

        GL11.glFlush();
        GL11.glFinish();
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        int[] data = new int[3];
        GL11.glReadPixels((int) x, (int) y, 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, data);

        framebuffer.unbind();

        if (rendered) {
            int pickedId = data[0] + data[1] * 256 + data[2] * 256 * 256;
            if (pickedId == 0 || pickedId < 0 || pickedId - 1 >= SceneManager.getCurrentScene().getObjects().size()) {
                return null;
            } else {
                return SceneManager.getCurrentScene().getObjects().get(pickedId - 1);
            }
        }

        rendered = true;
        return null;
    }

    public static int getId() {
        return framebuffer.getTextureID();
    }

    private static float inverseLerp(float imin, float imax, float omin, float omax, float v) {
        float t = (v - imin) / (imax - imin);
        float lerp = (1f - t) * omin + omax * t;

        return lerp;
    }

}
