package org.landon.scene;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private List<GameObject> objects;
    private Camera camera;

    public Scene() {
        objects = new ArrayList<>();
        camera = new Camera();
    }

    public void update() {
        camera.update();
        for (GameObject object : objects) {
            object.update();
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

    public List<GameObject> getObjects() {
        return objects;
    }

    public Camera getCamera() {
        return camera;
    }

}
