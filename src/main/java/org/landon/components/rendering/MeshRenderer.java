package org.landon.components.rendering;

import org.landon.annoations.RunInEditMode;
import org.landon.components.Component;
import org.landon.components.graphics.MeshFilter;
import org.landon.graphics.renderers.Renderer;
import org.landon.graphics.renderers.Renderers;

import java.lang.reflect.Field;

@RunInEditMode
public class MeshRenderer extends Component {

    private Renderers.RendererType rendererType = Renderers.RendererType.UNLIT;

    private transient Renderer renderer = Renderers.getRenderer(rendererType);
    private transient MeshFilter meshFilter;

    public MeshRenderer() {
        super("Mesh Renderer", false, true);
    }

    @Override
    public void update() {
        meshFilter = gameObject.getComponent(MeshFilter.class);
        renderer.render(meshFilter);
    }

    @Override
    public void variableUpdated(Field field) {
        if (field.getName().equals("rendererType")) {
            renderer = Renderers.getRenderer(rendererType);
        }
    }

    public void setRendererType(Renderers.RendererType rendererType) {
        this.rendererType = rendererType;
        renderer = Renderers.getRenderer(rendererType);
    }

}
