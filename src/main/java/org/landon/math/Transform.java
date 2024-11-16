package org.landon.math;

import com.alibaba.fastjson.annotation.JSONField;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    
    private Vector3f localPosition, localRotation, localScale;
    private transient Vector3f worldPosition, worldRotation, worldScale;

    public Transform() {
        localPosition = new Vector3f(0);
        localRotation = new Vector3f(0);
        localScale = new Vector3f(1);

        worldPosition = new Vector3f(0);
        worldRotation = new Vector3f(0);
        worldScale = new Vector3f(1);
    }

    public Transform(Vector3f localPosition, Vector3f localRotation, Vector3f localScale) {
        this.localPosition = localPosition;
        this.localRotation = localRotation;
        this.localScale = localScale;

        worldPosition = new Vector3f();
        worldRotation = new Vector3f();
        worldScale = new Vector3f();
    }

    public void update(Transform parent) {
        if (parent == null) {
            worldPosition = localPosition;
            worldRotation = localRotation;
            worldScale = localScale;
        } else {
            worldPosition = parent.localPosition.add(localPosition, new Vector3f());
            worldRotation = parent.localRotation.add(localRotation, new Vector3f());
            worldScale = parent.localScale.mul(localScale, new Vector3f());
        }
    }

    public void reset() {
        localPosition.set(0);
        localRotation.set(0);
        localScale.set(1);
    }

    public Matrix4f getModelMatrix() {
        return new Matrix4f().translate(worldPosition).rotateX((float) Math.toRadians(worldRotation.x)).rotateY((float) Math.toRadians(worldRotation.y)).rotateZ((float) Math.toRadians(worldRotation.z)).scale(worldScale);
    }

    public Vector3f getForward() {
        float x = (float) Math.sin(Math.toRadians(localRotation.y)) * (float) Math.cos(Math.toRadians(localRotation.x));
        float y = (float) -Math.sin(Math.toRadians(localRotation.x));
        float z = (float) -Math.cos(Math.toRadians(localRotation.y)) * (float) Math.cos(Math.toRadians(localRotation.x));
        return new Vector3f(x, y, z);
    }

    public Vector3f getRight() {
        Vector3f forward = getForward();
        return forward.cross(new Vector3f(0, 1, 0)).normalize();
    }

    public Vector3f getLocalPosition() {
        return localPosition;
    }

    public void setLocalPosition(Vector3f newPosition) {
        localPosition = newPosition;
    }

    public void setLocalPosition(float x, float y, float z) {
        localPosition.set(x, y, z);
    }

    public Vector3f getLocalRotation() {
        return localRotation;
    }

    public void setLocalRotation(Vector3f newLocalRotation) {
        localRotation = newLocalRotation;
    }

    public void setLocalRotation(float x, float y, float z) {
        localRotation.set(x, y, z);
    }

    public Vector3f getLocalScale() {
        return localScale;
    }

    public void setLocalScale(Vector3f newLocalScale) {
        localScale = newLocalScale;
    }

    public void setLocalScale(float x, float y, float z) {
        localScale.set(x, y, z);
    }

    public void setWorldPosition(Vector3f newWorldPosition, Transform parent) {
        Vector3f parentWorldPosition = parent == null ? new Vector3f(0) : parent.getWorldPosition();
        localPosition = newWorldPosition.sub(parentWorldPosition, new Vector3f());
    }

    public void setWorldRotation(Vector3f newWorldRotation, Transform parent) {
        Vector3f parentWorldRotation = parent == null ? new Vector3f(0) : parent.getWorldRotation();
        localRotation = newWorldRotation.sub(parentWorldRotation, new Vector3f());
    }

    public void setWorldScale(Vector3f newWorldScale, Transform parent) {
        Vector3f parentWorldScale = parent == null ? new Vector3f(1) : parent.getWorldScale();
        localScale = newWorldScale.div(parentWorldScale, new Vector3f());
    }

    public void setWorldPosition(float x, float y, float z, Transform parent) {
        setWorldPosition(new Vector3f(x, y, z), parent);
    }

    public void setWorldRotation(float x, float y, float z, Transform parent) {
        setWorldRotation(new Vector3f(x, y, z), parent);
    }

    public void setWorldScale(float x, float y, float z, Transform parent) {
        setWorldScale(new Vector3f(x, y, z), parent);
    }

    public Vector3f getWorldPosition() {
        return worldPosition;
    }

    public Vector3f getWorldRotation() {
        return worldRotation;
    }

    public Vector3f getWorldScale() {
        return worldScale;
    }

    public Transform clone() {
        return new Transform(new Vector3f(localPosition), new Vector3f(localRotation), new Vector3f(localScale));
    }

}
