package org.landon.editor.gizmos;

import org.joml.Matrix4f;
import org.landon.editor.Editor;
import org.landon.editor.scene.EditorObject;
import org.landon.graphics.mesh.Mesh;
import org.landon.graphics.mesh.Meshes;
import org.landon.graphics.shaders.Shader;
import org.landon.scene.GameObject;
import org.landon.scene.SceneManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class ComponentGizmo extends EditorObject {

    private final GameObject gameObject;
    private final Mesh mesh;
    private final int texture;
    private final Shader shader;

    public ComponentGizmo(GameObject gameObject, int texture) {
        this.gameObject = gameObject;
        this.texture = texture;

        this.mesh = Meshes.createPlane();
        shader = new Shader("resources/shaders/gizmo/component/vert.glsl", "resources/shaders/gizmo/component/frag.glsl");

        Editor.getScene().addObject(this);
    }

    @Override
    public void update() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL30.glBindVertexArray(mesh.getVao());

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, texture);

        shader.bind();

        Matrix4f model = gameObject.getTransform().getModelMatrix();
        Matrix4f view = SceneManager.getViewMatrix();
        model.m00(view.m00());
        model.m01(view.m10());
        model.m02(view.m20());
        model.m10(view.m01());
        model.m11(view.m11());
        model.m12(view.m21());
        model.m20(view.m02());
        model.m21(view.m12());
        model.m22(view.m22());
        model.scale(1, 1, 1);

        shader.setUniform("model", model);
        shader.setUniform("view", view);
        shader.setUniform("projection", SceneManager.getProjectionMatrix());
        shader.setUniform("tex", 0);

        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);

        shader.unbind();

        GL13.glActiveTexture(0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

}
