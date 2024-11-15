package org.landon.editor.gizmos;

import imgui.extension.imguizmo.ImGuizmo;
import imgui.extension.imguizmo.flag.Mode;
import imgui.extension.imguizmo.flag.Operation;
import org.joml.Vector2f;
import org.landon.editor.Editor;
import org.landon.editor.windows.viewport.Viewport;
import org.landon.editor.windows.inspector.Inspector;

public class TransformationGizmo {

    private static int operation = Operation.TRANSLATE;

    public static boolean update() {
        if (Editor.isPlaying() || Inspector.getSelectedObject() == null) return false;
        setupImGuizmo();

        float[] cameraProjection = Editor.getCamera().getProjection().get(new float[16]);
        float[] cameraView = Editor.getCamera().getViewMatrix().get(new float[16]);
        float[] model = Inspector.getSelectedObject().getTransform().getModelMatrix().get(new float[16]);

        ImGuizmo.manipulate(cameraView, cameraProjection, model, operation, Mode.LOCAL);
        if (ImGuizmo.isUsing()) {
            float[] position = new float[3];
            float[] rotation = new float[3];
            float[] scale = new float[3];
            ImGuizmo.decomposeMatrixToComponents(model, position, rotation, scale);

            Inspector.getSelectedObject().getTransform().setWorldPosition(position[0], position[1], position[2], Inspector.getSelectedObject().getParentTransform());
            Inspector.getSelectedObject().getTransform().setWorldRotation(rotation[0], rotation[1], rotation[2], Inspector.getSelectedObject().getParentTransform());
            Inspector.getSelectedObject().getTransform().setWorldScale(scale[0], scale[1], scale[2], Inspector.getSelectedObject().getParentTransform());

            return true;
        }

        return false;
    }

    private static void setupImGuizmo() {
        ImGuizmo.setOrthographic(false);
        ImGuizmo.setEnabled(true);
        ImGuizmo.setDrawList();

        Vector2f imagePosition = Viewport.getImagePosition();
        Vector2f imageSize = Viewport.getImageSize();
        Vector2f viewportPosition = Viewport.getPosition();
        ImGuizmo.setRect(viewportPosition.x + imagePosition.x, viewportPosition.y + imagePosition.y, imageSize.x, imageSize.y);
    }

}
