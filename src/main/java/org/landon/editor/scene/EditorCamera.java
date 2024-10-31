package org.landon.editor.scene;

import org.joml.Matrix4f;
import org.landon.core.Window;
import org.landon.math.Transform;

public class EditorCamera {

    private float fov = 70;
    private float nearPlane = 0.01f, farPlane = 1000.0f;
    private final Transform transform = new Transform();

    public Matrix4f getViewMatrix() {
        return new Matrix4f().identity().rotateX((float) Math.toRadians(transform.getWorldRotation().x)).rotateY((float) Math.toRadians(transform.getWorldRotation().y)).rotateZ((float) Math.toRadians(transform.getWorldRotation().z)).translate(-transform.getWorldPosition().x, -transform.getWorldPosition().y, -transform.getWorldPosition().z);
    }

    public Matrix4f getProjection() {
        Window window = Window.getInstance();
        float aspect = (float) window.getWidth() / (float) window.getHeight();
        return new Matrix4f().identity().perspective((float) Math.toRadians(fov), aspect, nearPlane, farPlane);
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public float getFov() {
        return fov;
    }

    public void setNearPlane(float nearPlane) {
        this.nearPlane = nearPlane;
    }

    public float getNearPlane() {
        return nearPlane;
    }

    public void setFarPlane(float farPlane) {
        this.farPlane = farPlane;
    }

    public float getFarPlane() {
        return farPlane;
    }
    
}
