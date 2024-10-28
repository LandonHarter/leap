package org.landon.scene;

import org.landon.components.Camera;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private List<GameObject> objects;
    private Camera camera;

    public Scene() {
        objects = new ArrayList<>();
    }

    public void start() {
        checkForCamera();
        for (GameObject object : objects) {
            object.start();
        }
    }

    public void update() {
        for (GameObject object : objects) {
            object.update();
        }
    }

    public void destroy() {
        for (GameObject object : objects) {
            object.destroy();
        }
    }

    public void addObject(GameObject object) {
        objects.add(object);
        object.setScene(this);
    }

    public void removeObject(GameObject object) {
        objects.remove(object);
        object.setScene(null);
    }

    private void checkForCamera() {
        boolean hasCamera = false;
        for (GameObject object : objects) {
            if (object.getComponent(Camera.class) != null) {
                hasCamera = true;
                break;
            }
        }

        if (!hasCamera) {
            GameObject camera = new GameObject();
            camera.addComponent(new Camera());
            addObject(camera);
        }
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

}
