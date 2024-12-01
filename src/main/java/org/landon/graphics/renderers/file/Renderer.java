package org.landon.graphics.renderers.file;

import org.landon.serialization.Serializer;
import org.landon.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private String name;
    private String vertexFile, fragmentFile;
    private final List<ShaderBuffer> buffers;
    private final List<Uniform<?>> uniforms;

    public Renderer(String name, String vertexFile, String fragmentFile, List<ShaderBuffer> buffers, List<Uniform<?>> uniforms) {
        this.name = name;
        this.vertexFile = vertexFile;
        this.fragmentFile = fragmentFile;
        this.buffers = buffers;
        this.uniforms = uniforms;
    }

    public String getName() {
        return name;
    }

    public String getVertexFile() {
        return vertexFile;
    }

    public String getFragmentFile() {
        return fragmentFile;
    }

    public List<ShaderBuffer> getBuffers() {
        return buffers;
    }

    public List<Uniform<?>> getUniforms() {
        return uniforms;
    }

    public void save(File file) {
        FileUtil.writeFile(file, Serializer.toJson(this));
    }

    public static Renderer createRenderer(String name, String vertexFile, String fragmentFile) {
        List<ShaderBuffer> buffers = new ArrayList<>() {
            {
                add(new ShaderBuffer("position", 0));
                add(new ShaderBuffer("uvs", 1));
                add(new ShaderBuffer("normals", 2));
            }
        };
        List<Uniform<?>> uniforms = new ArrayList<>();

        String vertexSource = FileUtil.readFile(new File(vertexFile));
        String fragmentSource = FileUtil.readFile(new File(fragmentFile));

        parseUniforms(vertexSource, uniforms);
        parseUniforms(fragmentSource, uniforms);

        return new Renderer(name, vertexFile, fragmentFile, buffers, uniforms);
    }

    private static void parseUniforms(String source, List<Uniform<?>> uniforms) {
        for (String line : source.split("\n")) {
            if (line.startsWith("uniform")) {
                String[] tokens = line.split(" ");
                String uniformType = tokens[1];
                String uniformName = tokens[2].substring(0, tokens[2].length() - 1);
                uniforms.add(Uniform.createUniform(uniformName, uniformType));
            }
        }
    }

    public static Renderer loadRenderer(File file) {
        return Serializer.fromJson(FileUtil.readFile(file), Renderer.class);
    }

}
