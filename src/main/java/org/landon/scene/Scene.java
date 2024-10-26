package org.landon.scene;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private List<SceneObject> objects;
    private Camera camera;

    public Scene() {
        objects = new ArrayList<>();
        camera = new Camera();
    }

    public void addObject(SceneObject object) {
        objects.add(object);
        object.setScene(this);
    }

    public void removeObject(SceneObject object) {
        objects.remove(object);
        object.setScene(null);
    }

    public List<SceneObject> getObjects() {
        return objects;
    }

    public Camera getCamera() {
        return camera;
    }

}
