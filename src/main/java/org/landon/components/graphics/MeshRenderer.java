package org.landon.components.graphics;

import com.alibaba.fastjson.annotation.JSONField;
import org.landon.components.Component;
import org.landon.graphics.renderers.Renderer;
import org.landon.graphics.renderers.Renderers;

public class MeshRenderer extends Component {

    private transient Renderer renderer = Renderers.UNLIT_RENDERER;
    private transient MeshFilter meshFilter;

    public MeshRenderer() {
        super("Mesh Renderer", false);
    }

    public MeshRenderer(Renderer renderer) {
        super("Mesh Renderer", false);
        this.renderer = renderer;
    }

    @Override
    public void update() {
        meshFilter = gameObject.getComponent(MeshFilter.class);
        renderer.render(meshFilter);
    }

}
