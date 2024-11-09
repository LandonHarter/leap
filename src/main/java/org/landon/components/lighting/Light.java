package org.landon.components.lighting;

import org.landon.components.Component;
import org.landon.graphics.Color;
import org.landon.graphics.shaders.Shader;
import org.landon.scene.SceneManager;

public class Light extends Component {

    private float intensity = 1.0f;
    private float attenuation = 0.0f;
    private Color color = new Color(1, 1, 1);

    public Light() {
        super("Point Light", false, true);
    }

    @Override
    public void update() {

    }

    public void updateUniforms(Shader shader, int index) {
        if (!isEnabled()) return;
        String name = "lights[" + index + "]";
        shader.setUniform(name + ".position", getGameObject().getTransform().getWorldPosition());
        shader.setUniform(name + ".intensity", intensity);
        shader.setUniform(name + ".color", color.getRgb());
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

}
