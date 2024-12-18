package org.landon.components.lighting;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.landon.annoations.RunInEditMode;
import org.landon.components.Component;
import org.landon.editor.Icons;
import org.landon.editor.gizmos.ComponentGizmo;
import org.landon.editor.windows.logger.Logger;
import org.landon.graphics.renderers.Renderer;
import org.landon.graphics.renderers.RendererType;
import org.landon.graphics.renderers.Renderers;
import org.landon.graphics.shaders.Shader;
import org.landon.scene.SceneManager;
import org.landon.serialization.types.LeapFloat;

@RunInEditMode
public class Light extends Component {

    private final LeapFloat intensity = new LeapFloat(1);
    private final LeapFloat attenuation = new LeapFloat(0.015f);
    private final Vector4f color = new Vector4f(1, 1, 1, 1);

    public Light() {
        super("Light", false, true);
    }

    public void updateUniforms(Shader shader, int index) {
        String name = "lights[" + index + "]";
        shader.setUniform(name + ".position", getGameObject().getTransform().getWorldPosition());
        shader.setUniform(name + ".intensity", intensity.getValue());
        shader.setUniform(name + ".color", color.xyz(new Vector3f()));
        shader.setUniform(name + ".attenuation", attenuation.getValue());
    }

    public void resetUniforms(Shader shader, int index) {
        String name = "lights[" + index + "]";
        shader.setUniform(name + ".position", new Vector3f());
        shader.setUniform(name + ".intensity", 0.0f);
        shader.setUniform(name + ".color", new Vector3f());
        shader.setUniform(name + ".attenuation", 0.0f);
    }

    public void setUniforms(Shader shader, int index) {
        if (isEnabled() && gameObject.isEnabled()) {
            updateUniforms(shader, index);
        } else {
            resetUniforms(shader, index);
        }
    }

    @Override
    public void onAdd() {
        if (SceneManager.getCurrentScene() == null) return;
        SceneManager.getCurrentScene().addLight(this);
    }

    @Override
    public void onRemove() {
        if (SceneManager.getCurrentScene() == null) return;
        Shader shader = Renderers.getRenderer(RendererType.LIT).getShader();
        shader.bind();
        for (int i = 0; i < SceneManager.getCurrentScene().getLights().size(); i++) {
            resetUniforms(shader, i);
        }
        shader.unbind();
        gizmo.destroy();
    }

    @Override
    public void createGizmo() {
        if (gizmoCreated) return;
        gizmoCreated = true;

        gizmo = new ComponentGizmo(gameObject, Icons.getIcon("light"));
    }
}
