package org.landon.graphics.renderers.file;

public class ShaderBuffer {

    private final String name;
    private final int bufferId;

    public ShaderBuffer(String name, int bufferId) {
        this.name = name;
        this.bufferId = bufferId;
    }

    public String getName() {
        return name;
    }

    public int getBufferId() {
        return bufferId;
    }

}
