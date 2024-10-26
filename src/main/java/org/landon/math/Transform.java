package org.landon.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {

    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform() {
        position = new Vector3f(0);
        rotation = new Vector3f(0);
        scale = new Vector3f(1);
    }

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Matrix4f getModelMatrix() {
        return new Matrix4f().translate(position).rotateX((float) Math.toRadians(rotation.x)).rotateY((float) Math.toRadians(rotation.y)).rotateZ((float) Math.toRadians(rotation.z)).scale(scale);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f newPosition) {
        position = newPosition;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f newRotation) {
        rotation = newRotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f newScale) {
        scale = newScale;
    }

}
