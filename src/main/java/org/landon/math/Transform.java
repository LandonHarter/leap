package org.landon.math;

import com.alibaba.fastjson.annotation.JSONField;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {

    @JSONField(name = "position")
    private Vector3f position;

    @JSONField(name = "rotation")
    private Vector3f rotation;

    @JSONField(name = "scale")
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

    public Vector3f getForward() {
        float x = (float) Math.sin(Math.toRadians(rotation.y)) * (float) Math.cos(Math.toRadians(rotation.x));
        float y = (float) -Math.sin(Math.toRadians(rotation.x));
        float z = (float) -Math.cos(Math.toRadians(rotation.y)) * (float) Math.cos(Math.toRadians(rotation.x));
        return new Vector3f(x, y, z);
    }

    public Vector3f getRight() {
        Vector3f forward = getForward();
        return forward.cross(new Vector3f(0, 1, 0)).normalize();
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
