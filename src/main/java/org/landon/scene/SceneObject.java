package org.landon.scene;

import org.joml.Vector3f;
import org.landon.core.Time;
import org.landon.graphics.Material;
import org.landon.graphics.Mesh;
import org.landon.math.Transform;
import org.landon.physics.MovingBody;

public class SceneObject {

    private Scene scene;

    private Transform transform;
    private Mesh mesh;
    private Material material;

    private MovingBody movingBody;

    public SceneObject() {
        transform = new Transform();
    }

    public SceneObject(Mesh mesh) {
        this.mesh = mesh;
        transform = new Transform();
        material = new Material();
        movingBody = new MovingBody(transform, 1.0f);
    }

    public SceneObject(Mesh mesh, Transform transform) {
        this.mesh = mesh;
        this.transform = transform;
        this.material = new Material();
        movingBody = new MovingBody(transform, 1.0f);
    }

    public SceneObject(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
        transform = new Transform();
        movingBody = new MovingBody(transform, 1.0f);
    }

    public SceneObject(Mesh mesh, Material material, Transform transform) {
        this.mesh = mesh;
        this.material = material;
        this.transform = transform;
        movingBody = new MovingBody(transform, 1.0f);
    }

    public void update() {
        movingBody.update();
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public MovingBody getMovingBody() {
        return movingBody;
    }

}
