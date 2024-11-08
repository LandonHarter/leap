package org.landon.graphics.renderers;

import java.util.List;

public final class Renderers {

    private static final List<Renderer> RENDERERS = List.of(
        new UnlitRenderer(),
        new LitRenderer()
    );

    public static Renderer getRenderer(RendererType type) {
        return RENDERERS.get(type.ordinal());
    }

    public enum RendererType {
        UNLIT,
        LIT
    }

}
