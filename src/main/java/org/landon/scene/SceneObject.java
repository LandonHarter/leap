package org.landon.scene;

import org.landon.graphics.Mesh;
import org.landon.math.Transform;

public class SceneObject {

    private Scene scene;

    private Transform transform;
    private Mesh mesh;

    public SceneObject() {
        transform = new Transform();
    }

    public SceneObject(Mesh mesh) {
        this.mesh = mesh;
        transform = new Transform();
    }

    public SceneObject(Mesh mesh, Transform transform) {
        this.mesh = mesh;
        this.transform = transform;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

}
