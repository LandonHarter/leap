package org.landon.components.graphics;

import org.joml.Vector3f;
import org.landon.annoations.HideField;
import org.landon.annoations.RunInEditMode;
import org.landon.components.Component;
import org.landon.frustum.AABB;
import org.landon.frustum.Frustum;
import org.landon.graphics.material.Material;
import org.landon.graphics.mesh.Mesh;

@RunInEditMode
public class MeshFilter extends Component {

    private Mesh mesh;
    private Material material;

    @HideField
    private transient boolean inFrustum = false;
    private transient AABB aabb;

    public MeshFilter() {
        super("Mesh Filter", false, false);
        this.material = new Material();
    }

    public MeshFilter(Mesh mesh) {
        super("Mesh Filter", false, false);
        this.mesh = mesh;
        this.material = new Material();
    }

    public MeshFilter(Mesh mesh, Material material) {
        super("Mesh Filter", false, false);
        this.mesh = mesh;
        this.material = material;
    }

    @Override
    public void onAdd() {
        calculateAabb();
    }

    @Override
    public void update() {
        inFrustum = Frustum.inFrustum(aabb);
    }

    @Override
    public void onTransformChange() {
        calculateAabb();
    }

    private void calculateAabb() {
        if (gameObject == null) return;
        Vector3f min = new Vector3f(Float.MAX_VALUE);
        Vector3f max = new Vector3f(Float.MIN_VALUE);
        Vector3f scale = gameObject.getTransform().getWorldScale();

        if (mesh == null) {
            aabb = new AABB(min, max);
            return;
        }

        for (Mesh.Vertex vertex : mesh.getVertices()) {
            Vector3f position = new Vector3f(vertex.getPosition());
            position.mul(scale);

            if (position.x < min.x) min.x = position.x;
            if (position.y < min.y) min.y = position.y;
            if (position.z < min.z) min.z = position.z;

            if (position.x > max.x) max.x = position.x;
            if (position.y > max.y) max.y = position.y;
            if (position.z > max.z) max.z = position.z;
        }

        min.add(gameObject.getTransform().getWorldPosition());
        max.add(gameObject.getTransform().getWorldPosition());
        aabb = new AABB(min, max);
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
        calculateAabb();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isInFrustum() {
        return inFrustum;
    }

}
