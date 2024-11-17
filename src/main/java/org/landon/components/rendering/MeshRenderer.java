package org.landon.components.rendering;

import org.landon.annoations.RunInEditMode;
import org.landon.components.Component;
import org.landon.components.graphics.MeshFilter;
import org.landon.editor.Editor;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.graphics.renderers.RenderQueue;
import org.landon.graphics.renderers.Renderer;
import org.landon.graphics.renderers.RendererType;
import org.landon.graphics.renderers.Renderers;
import org.landon.serialization.types.LeapEnum;

import java.lang.reflect.Field;

@RunInEditMode
public class MeshRenderer extends Component {

    private final LeapEnum<RendererType> rendererType = new LeapEnum<>(RendererType.LIT);

    private transient Renderer renderer;
    private transient MeshFilter meshFilter;

    public MeshRenderer() {
        super("Mesh Renderer", false, true);
        renderer = Renderers.getRenderer(rendererType.getValue());
    }

    @Override
    public void load() {
        renderer = Renderers.getRenderer(rendererType.getValue());
    }

    @Override
    public void update() {
        meshFilter = gameObject.getComponent(MeshFilter.class);
        boolean transparent = meshFilter.getMaterial().hasTransparency();
        if (transparent) RenderQueue.addTransparentRenderer(this);
        else RenderQueue.addOpaqueRenderer(this);
    }

    @Override
    public void variableUpdated(Field field) {
        if (field.getName().equals("rendererType")) {
            renderer = Renderers.getRenderer(rendererType.getValue());
        }
    }

    public void setRendererType(RendererType rendererType) {
        this.rendererType.setValue(rendererType);
        renderer = Renderers.getRenderer(rendererType);
    }

    public void render() {
        renderer.render(meshFilter, gameObject.equals(Inspector.getSelectedObject()) && !Editor.isPlaying());
    }

}
