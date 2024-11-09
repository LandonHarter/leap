package org.landon.scene;

import org.landon.components.lighting.Light;
import org.landon.components.rendering.Camera;
import org.landon.editor.Editor;
import org.landon.skybox.Skybox;
import org.landon.skybox.SkyboxType;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Scene {

    private transient File file;
    private final String name;
    private final List<GameObject> objects;

    private final Skybox skybox;

    private transient Camera camera;
    private transient List<Light> lights;

    public Scene(String name, boolean load) {
        objects = new ArrayList<>();
        this.name = name;

        if (load) SceneManager.loadScene(this);
        skybox = new Skybox(SkyboxType.Cubemap);
        lights = new LinkedList<>();
    }

    public Scene() {
        this("Scene", true);
    }

    public void start() {
        for (GameObject object : objects) {
            object.start();
        }
    }

    public void editorStart() {
        for (GameObject object : objects) {
            object.editorStart();
        }
    }

    public void update() {
        skybox.render();
        for (GameObject object : objects) {
            if (object.getParent() == null) {
                object.update(); // Only update root objects
            }
        }
        checkForCamera();
    }

    public void destroy() {
        for (GameObject object : objects) {
            object.destroy();
        }
    }

    public void addObject(GameObject object) {
        objects.add(object);
        object.setScene(this);
        object.onAddToScene();
    }

    public void removeObject(GameObject object) {
        object.onRemoveFromScene();
        object.destroy();
        objects.remove(object);
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

    public void addLight(Light light) {
        lights.add(light);
    }

    public void removeLight(Light light) {
        lights.remove(light);
    }

    public List<Light> getLights() {
        return lights;
    }

    public void save(String path) {
        SceneManager.saveScene(this, new File(path));
    }

    public void save() {
        if (file == null) return;
        save(file.getAbsolutePath());
    }

    public void checkForCamera() {
        for (GameObject object : objects) {
            if (object.getComponent(Camera.class) != null) {
                camera = object.getComponent(Camera.class);
                break;
            }
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

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Skybox getSkybox() {
        return skybox;
    }

}
