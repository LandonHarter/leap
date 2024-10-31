package org.landon.scene;

import org.landon.components.Camera;
import org.landon.editor.windows.SceneHierarchy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Scene {

    private transient File file;
    private final String name;
    private final List<GameObject> objects;

    private transient Camera camera;

    public Scene(String name, boolean load) {
        objects = new ArrayList<>();
        this.name = name;

        if (load) SceneManager.loadScene(this);
    }

    public Scene() {
        this("Scene", true);
    }

    public void start() {
        checkForCamera();
        for (GameObject object : objects) {
            object.start();
        }
    }

    public void update() {
        for (GameObject object : objects) {
            if (object.getParent() == null) object.update(); // Only update root objects
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

    public void moveObject(GameObject object, int index) {
        objects.remove(object);
        objects.add(index, object);
    }

    public void moveObject(GameObject object, GameObject after) {
        objects.remove(object);
        objects.add(objects.indexOf(after) + 1, object);
    }

    public GameObject findObject(String uuid) {
        for (GameObject object : objects) {
            if (object.getUuid().equals(uuid)) {
                return object;
            }
        }
        return null;
    }

    public void addCamera() {
        GameObject camera = new GameObject("Camera");
        camera.addComponent(new Camera());
        addObject(camera);
    }

    private void checkForCamera() {
        boolean hasCamera = false;
        for (GameObject object : objects) {
            if (object.getComponent(Camera.class) != null) {
                hasCamera = true;
                break;
            }
        }

        if (!hasCamera) addCamera();
    }

    public void save(String path) {
        SceneManager.saveScene(this, new File(path));
    }

    public void save() {
        if (file == null) return;
        save(file.getAbsolutePath());
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

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
