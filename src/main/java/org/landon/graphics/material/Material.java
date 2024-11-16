package org.landon.graphics.material;

import org.joml.Vector4f;

public class Material {

    private Texture texture;
    private Vector4f color;
    private float shineDamper = 1;
    private float reflectivity = 0;

    private transient boolean hasTransparency = false;

    public Material() {
        this.texture = new Texture("resources/textures/default.png");
        this.color = new Vector4f(1, 1, 1, 1);
        hasTransparency = false;
    }

    public Material(Texture texture) {
        this.texture = texture;
        this.color = new Vector4f(1, 1, 1, 1);
        hasTransparency = texture.isTransparent() || color.w < 1;
    }

    public Material(Texture texture, Vector4f color) {
        this.texture = texture;
        this.color = color;
        hasTransparency = texture.isTransparent() || color.w < 1;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        hasTransparency = texture.isTransparent();
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
        hasTransparency = color.w < 1 || texture.isTransparent();
    }

    public void setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
        hasTransparency = a < 1 || texture.isTransparent();
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
