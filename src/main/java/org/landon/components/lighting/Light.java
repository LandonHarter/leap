package org.landon.components.lighting;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.landon.components.Component;
import org.landon.editor.Icons;
import org.landon.editor.gizmos.ComponentGizmo;
import org.landon.graphics.shaders.Shader;
import org.landon.scene.SceneManager;

public class Light extends Component {

    private float intensity = 1.0f;
    private float attenuation = 0.0f;
    private Vector4f color = new Vector4f(1, 1, 1, 1);

    public Light() {
        super("Light", false, true);
    }

    public void updateUniforms(Shader shader, int index) {
        if (!isEnabled()) return;
        String name = "lights[" + index + "]";
        shader.setUniform(name + ".position", getGameObject().getTransform().getWorldPosition());
        shader.setUniform(name + ".intensity", intensity);
        shader.setUniform(name + ".color", color.xyz(new Vector3f()));
        shader.setUniform(name + ".attenuation", attenuation);
    }

    @Override
    public void onAdd() {
        if (SceneManager.getCurrentScene() == null) return;
        SceneManager.getCurrentScene().addLight(this);
    }

    @Override
    public void onRemove() {
        if (SceneManager.getCurrentScene() == null) return;
        SceneManager.getCurrentScene().removeLight(this);
    }

    @Override
    public void createGizmo() {
        gizmo = new ComponentGizmo(gameObject, Icons.getIcon("light"));
    }
}
