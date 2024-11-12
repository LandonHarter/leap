package org.landon.editor.scene;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.landon.core.Time;
import org.landon.core.Window;
import org.landon.editor.Editor;
import org.landon.input.Input;
import org.landon.math.Transform;

public class EditorCamera {

    private float fov = 70;
    private float nearPlane = 0.01f, farPlane = 1000.0f;
    private final Transform transform = new Transform();

    private Vector3f targetDest = new Vector3f();
    private final Vector2f oldMousePos = new Vector2f();
    private final Vector2f newMousePos = new Vector2f();

    private final float moveSpeed = 40;
    private final float sensitivity = 8;

    public void movement(boolean viewportHovered) {
        transform.setLocalPosition(transform.getLocalPosition().lerp(targetDest, 0.1f));
        if (Editor.isPlaying() || !viewportHovered)
            return;

        newMousePos.set(Input.getMouseX(), Input.getMouseY());
        if (Input.getScrollY() != 0) {
            if (Input.getScrollY() > 0)
                targetDest = transform.getForward().mul((float) Time.getDelta() * moveSpeed).add(targetDest);
            else
                targetDest = transform.getForward().mul((float) Time.getDelta() * -moveSpeed).add(targetDest);
            Input.resetScroll();
        }

        if (Input.isButtonDown(1)) {
            float dx = newMousePos.x - oldMousePos.x;
            float dy = newMousePos.y - oldMousePos.y;

            transform.setLocalRotation(transform.getLocalRotation().sub(new Vector3f(dy * sensitivity, dx * sensitivity, 0).mul((float) Time.getDelta())));
        }
        oldMousePos.set(newMousePos);

        Editor.getSettings().setCameraPosition(transform.getLocalPosition());
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().identity().rotateX((float) Math.toRadians(transform.getLocalRotation().x)).rotateY((float) Math.toRadians(transform.getLocalRotation().y)).rotateZ((float) Math.toRadians(transform.getLocalRotation().z)).translate(-transform.getLocalPosition().x, -transform.getLocalPosition().y, -transform.getLocalPosition().z);
    }

    public Matrix4f getProjection() {
        Window window = Window.getInstance();
        float aspect = (float) window.getWidth() / (float) window.getHeight();
        return new Matrix4f().identity().perspective((float) Math.toRadians(fov), aspect, nearPlane, farPlane);
    }

    public Transform getTransform() {
        return transform;
    }

    public void setPosition(Vector3f position) {
        transform.setLocalPosition(position);
        targetDest = position;
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
