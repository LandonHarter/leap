package org.landon.scene;

import org.joml.Matrix4f;
import org.landon.math.Transform;

public class Camera {

    private Transform transform;

    private float fov = 70.0f;
    private float nearPlane = 0.01f, farPlane = 1000.0f;

    public Camera() {
        transform = new Transform();
    }

    public Camera(Transform transform) {
        this.transform = transform;
    }

    public Camera(Transform transform, float fov) {
        this.transform = transform;
        this.fov = fov;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().identity().rotateX((float) Math.toRadians(transform.getRotation().x)).rotateY((float) Math.toRadians(transform.getRotation().y)).rotateZ((float) Math.toRadians(transform.getRotation().z)).translate(-transform.getPosition().x, -transform.getPosition().y, -transform.getPosition().z);
    }

    public Matrix4f getProjection() {
        return new Matrix4f().identity().perspective((float) Math.toRadians(fov), 800.0f / 500.0f, nearPlane, farPlane);
    }

    public Transform getTransform() {
        return transform;
    }

}
