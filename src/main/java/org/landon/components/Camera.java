package org.landon.components;

import org.joml.Matrix4f;
import org.landon.core.Window;

public class Camera extends Component {

    private float fov = 70;
    private float nearPlane = 0.01f, farPlane = 1000.0f;

    public Camera() {
        super("Camera", false);
    }

    @Override
    public void start() {
        gameObject.getScene().setCamera(this);
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().identity().rotateX((float) Math.toRadians(gameObject.getTransform().getWorldRotation().x)).rotateY((float) Math.toRadians(gameObject.getTransform().getWorldRotation().y)).rotateZ((float) Math.toRadians(gameObject.getTransform().getWorldRotation().z)).translate(-gameObject.getTransform().getWorldPosition().x, -gameObject.getTransform().getWorldPosition().y, -gameObject.getTransform().getWorldPosition().z);
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
