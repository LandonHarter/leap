package org.landon.graphics.material;

import org.joml.Vector4f;

public class Material {

    private Texture texture;
    private Vector4f color;
    private float shineDamper = 1;
    private float reflectivity = 0;

    public Material() {
        this.texture = new Texture("resources/textures/default.png");
        this.color = new Vector4f(1, 1, 1, 1);
    }

    public Material(Texture texture) {
        this.texture = texture;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    public Material(Texture texture, Vector4f color) {
        this.texture = texture;
        this.color = color;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
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
