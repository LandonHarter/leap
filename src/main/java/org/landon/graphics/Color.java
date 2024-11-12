package org.landon.graphics;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color {

    private Vector4f color;

    public Color(float r, float g, float b, float a) {
        color = new Vector4f(r, g, b, a);
    }

    public Color(float r, float g, float b) {
        this(r, g, b, 1.0f);
    }

    public Color(Vector4f color) {
        this.color = color;
    }

    public Vector3f getRgb() {
        return new Vector3f(color.x, color.y, color.z);
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public void setColor(float r, float g, float b, float a) {
        color.set(r, g, b, a);
    }

    public void setColor(float r, float g, float b) {
        setColor(r, g, b, 1.0f);
    }

    public void setRed(float r) {
        color.x = r;
    }

    public void setGreen(float g) {
        color.y = g;
    }

    public void setBlue(float b) {
        color.z = b;
    }

}
