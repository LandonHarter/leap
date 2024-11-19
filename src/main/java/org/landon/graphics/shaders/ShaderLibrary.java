package org.landon.graphics.shaders;

import org.landon.util.FileUtil;

import java.io.File;
import java.util.LinkedList;
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
        LinkedList<ShaderLibrary> lib = new LinkedList<>();
        lib.add(new ShaderLibrary("resources/shaders/libraries/types.glsl", ShaderType.FRAGMENT));
        lib.add(new ShaderLibrary("resources/shaders/libraries/math.glsl", ShaderType.ALL));
        return lib;
    }

    public enum ShaderType {
        VERTEX,
        GEOMETRY,
        FRAGMENT,
        ALL
    }

}
