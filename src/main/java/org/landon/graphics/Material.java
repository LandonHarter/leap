package org.landon.graphics;

import com.alibaba.fastjson.annotation.JSONField;
import org.joml.Vector3f;

public class Material {

    private Texture texture;
    private Vector3f color;
    private float shineDamper = 1;
    private float reflectivity = 0;

    public Material() {
        this.texture = new Texture("resources/textures/default.png");
        this.color = new Vector3f(1, 1, 1);
    }

    public Material(Texture texture) {
        this.texture = texture;
        this.color = new Vector3f(1, 1, 1);
    }

    public Material(Texture texture, Vector3f color) {
        this.texture = texture;
        this.color = color;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

}
