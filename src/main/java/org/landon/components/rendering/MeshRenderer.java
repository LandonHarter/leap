package org.landon.components.rendering;

import org.landon.components.Component;
import org.landon.components.graphics.MeshFilter;
import org.landon.graphics.renderers.Renderer;
import org.landon.graphics.renderers.Renderers;

public class MeshRenderer extends Component {

    private transient Renderer renderer = Renderers.UNLIT_RENDERER;
    private transient MeshFilter meshFilter;

    public MeshRenderer() {
        super("Mesh Renderer", false, true);
    }

    public MeshRenderer(Renderer renderer) {
        super("Mesh Renderer", false, true);
        this.renderer = renderer;
    }

    @Override
    public void update() {
        meshFilter = gameObject.getComponent(MeshFilter.class);
        renderer.render(meshFilter);
    }

}
