package org.landon.components.rendering;

import org.joml.Matrix4f;
import org.landon.components.Component;
import org.landon.core.Window;
import org.landon.editor.Icons;
import org.landon.editor.gizmos.ComponentGizmo;
import org.landon.serialization.types.LeapFloat;

public class Camera extends Component {

    private final LeapFloat fov = new LeapFloat(70);
    private final LeapFloat nearPlane = new LeapFloat(0.01f), farPlane = new LeapFloat(1000.0f);

    public Camera() {
        super("Camera", false, false);
    }

    @Override
    public void start() {
        gameObject.getScene().setCamera(this);
    }

    @Override
    public void createGizmo() {
        if (gizmoCreated) return;
        gizmoCreated = true;
        gizmo = new ComponentGizmo(gameObject, Icons.getIcon("camera"));
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().identity().rotateX((float) Math.toRadians(gameObject.getTransform().getWorldRotation().x)).rotateY((float) Math.toRadians(gameObject.getTransform().getWorldRotation().y)).rotateZ((float) Math.toRadians(gameObject.getTransform().getWorldRotation().z)).translate(-gameObject.getTransform().getWorldPosition().x, -gameObject.getTransform().getWorldPosition().y, -gameObject.getTransform().getWorldPosition().z);
    }

    public Matrix4f getProjection() {
        Window window = Window.getInstance();
        float aspect = (float) window.getWidth() / (float) window.getHeight();
        return new Matrix4f().identity().perspective((float) Math.toRadians(fov.getValue()), aspect, nearPlane.getValue(), farPlane.getValue());
    }

    public void setFov(float fov) {
        this.fov.setValue(fov);
    }

    public float getFov() {
        return fov.getValue();
    }

    public void setNearPlane(float nearPlane) {
        this.nearPlane.setValue(nearPlane);
    }

    public float getNearPlane() {
        return nearPlane.getValue();
    }

    public void setFarPlane(float farPlane) {
        this.farPlane.setValue(farPlane);
    }

    public float getFarPlane() {
        return farPlane.getValue();
    }

}
