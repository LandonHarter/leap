package org.landon.components;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.landon.core.Time;
import org.landon.core.Window;
import org.landon.input.Input;
import org.landon.math.Transform;
import org.lwjgl.glfw.GLFW;

public class Camera extends Component {

    private float fov = 70;
    private float nearPlane = 0.01f, farPlane = 1000.0f;

    private float cameraSpeed = 5.0f;
    private float sensitivity = 35.0f;

    public Camera() {
        super("Camera", false);
    }

    @Override
    public void start() {
        gameObject.getScene().setCamera(this);
    }

    private void movement() {
        Transform transform = gameObject.getTransform();
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
            transform.getPosition().add(transform.getForward().mul((float) Time.getDelta() * cameraSpeed));
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
            transform.getPosition().sub(transform.getForward().mul((float) Time.getDelta() * cameraSpeed));
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
            transform.getPosition().sub(transform.getRight().mul((float) Time.getDelta() * cameraSpeed));
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            transform.getPosition().add(transform.getRight().mul((float) Time.getDelta() * cameraSpeed));
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            transform.getPosition().add(0, (float) Time.getDelta() * cameraSpeed, 0);
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            transform.getPosition().sub(0, (float) Time.getDelta() * cameraSpeed, 0);
        }

        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            Input.lockCursor();
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            Input.unlockCursor();
        }

        if (Input.isCursorLocked()) {
            Vector2f mouseDelta = Input.getMouseDelta();
            transform.getRotation().add(mouseDelta.y * sensitivity * (float) Time.getDelta(), mouseDelta.x * sensitivity * (float) Time.getDelta(), 0);

            if (transform.getRotation().x < -80) {
                transform.getRotation().x = -80;
            }
        }
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().identity().rotateX((float) Math.toRadians(gameObject.getTransform().getRotation().x)).rotateY((float) Math.toRadians(gameObject.getTransform().getRotation().y)).rotateZ((float) Math.toRadians(gameObject.getTransform().getRotation().z)).translate(-gameObject.getTransform().getPosition().x, -gameObject.getTransform().getPosition().y, -gameObject.getTransform().getPosition().z);
    }

    public Matrix4f getProjection() {
        Window window = Window.getInstance();
        float aspect = (float) window.getWidth() / (float) window.getHeight();
        return new Matrix4f().identity().perspective((float) Math.toRadians(fov), aspect, nearPlane, farPlane);
    }

}
