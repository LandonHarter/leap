package org.landon.graphics.shaders;

import org.landon.util.FileUtil;

import java.io.File;
import java.util.List;

public class ShaderLibrary {

    private ShaderType type;
    private String source;

    public ShaderLibrary(String filepath, ShaderType type) {
        this.type = type;
        this.source = FileUtil.readFile(new File(filepath));
    }

    public String inject(String source) {
        return source.replace("#version 330 core", "#version 330 core\n" + this.source);
    }

    public ShaderType getType() {
        return type;
    }

    public static List<ShaderLibrary> getDefaultLibraries() {
        return List.of(
                new ShaderLibrary("resources/shaders/libraries/math.glsl", ShaderType.ALL)
        );
    }

    public enum ShaderType {
        VERTEX,
        FRAGMENT,
        ALL
    }

}
