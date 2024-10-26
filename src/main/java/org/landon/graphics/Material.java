package org.landon.graphics;

import org.joml.Vector3f;

public class Material {

    private Texture texture;
    private Vector3f color;

    public Material() {
        this.texture = null;
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

}
