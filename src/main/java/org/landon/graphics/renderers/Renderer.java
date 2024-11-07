package org.landon.graphics.renderers;
import org.landon.components.graphics.MeshFilter;
import org.landon.graphics.shaders.Shader;

public abstract class Renderer {

    protected final Shader shader;

    public Renderer() {
        shader = new Shader("resources/shaders/unlit/vert.glsl", "resources/shaders/unlit/frag.glsl");
    }

    public Renderer(Shader shader) {
        this.shader = shader;
    }

    public void render(MeshFilter meshFilter) {}

}
