package org.landon.components.graphics;

import com.alibaba.fastjson.annotation.JSONField;
import org.landon.components.Component;
import org.landon.graphics.Material;
import org.landon.graphics.Mesh;

public class MeshFilter extends Component {

    private Mesh mesh;
    private Material material;

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
    public void start() {

    }

    @Override
    public void update() {

    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
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

}
