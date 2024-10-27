package org.landon.components.graphics;

import org.landon.components.Component;
import org.landon.graphics.renderers.Renderer;
import org.landon.graphics.renderers.Renderers;

public class MeshRenderer extends Component {

    private Renderer renderer = Renderers.UNLIT_RENDERER;
    private MeshFilter meshFilter;

    public MeshRenderer() {
        super("Mesh Renderer", false);
    }

    public MeshRenderer(Renderer renderer) {
        super("Mesh Renderer", false);
        this.renderer = renderer;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        renderer.render(meshFilter);
    }

    @Override
    public void onAdd() {
        meshFilter = gameObject.getComponent(MeshFilter.class);
    }

    @Override
    public void onComponentAdded(Component component) {
        if (component instanceof MeshFilter) {
            meshFilter = (MeshFilter) component;
        }
    }

    @Override
    public void onComponentRemoved(Component component) {
        if (component instanceof MeshFilter) {
            meshFilter = null;
        }
    }
}
