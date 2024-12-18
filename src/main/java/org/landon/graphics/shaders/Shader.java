package org.landon.graphics.shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.landon.util.FileUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

public class Shader {

    private final String vertexContent, geometryContent, fragmentContent;
    private final File vertexFile, geometryFile, fragmentFile;

    private List<ShaderLibrary> libraries;

    private int vertexId, geometryId, fragmentId, programId;
    private boolean validated;

    private LinkedHashMap<String, Integer> uniforms = new LinkedHashMap<>();

    private static int currentProgram = 0;

    public Shader(String vertexPath, String geometryPath, String fragmentPath, boolean create) {
        vertexFile = new File(vertexPath);
        geometryFile = geometryPath == null ? null : new File(geometryPath);
        fragmentFile = new File(fragmentPath);
        vertexContent = FileUtil.readFile(vertexFile);
        geometryContent = geometryPath == null ? null : FileUtil.readFile(geometryFile);
        fragmentContent = FileUtil.readFile(fragmentFile);
        libraries = ShaderLibrary.getDefaultLibraries();

        if (create) createShader();
    }

    public Shader(String vertexPath, String fragmentPath, boolean create) {
        this(vertexPath, null, fragmentPath, create);
    }

    public Shader(String vertexPath, String fragmentPath) {
        this(vertexPath, fragmentPath, true);
    }

    public Shader addLibrary(ShaderLibrary library) {
        libraries.add(library);
        return this;
    }

    public void validate() {
        if (validated) return;

        GL20.glValidateProgram(programId);
        if (GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed to validate program: " + GL20.glGetProgramInfoLog(programId));
            return;
        }

        validated = true;
    }

    public void bind() {
        if (currentProgram == programId) return;

        GL20.glUseProgram(programId);
        currentProgram = programId;
    }

    public void unbind() {
        GL20.glUseProgram(0);
        currentProgram = 0;
    }

    private int getUniformLocation(String name) {
        if (uniforms.containsKey(name)) return uniforms.get(name);
        return uniforms.computeIfAbsent(name, k -> GL20.glGetUniformLocation(programId, k));
    }

    public void setUniform(String name, int value) {
        GL20.glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, float value) {
        GL20.glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, Vector2f value) {
        GL20.glUniform2f(getUniformLocation(name), value.x, value.y);
    }

    public void setUniform(String name, Vector3f value) {
        GL20.glUniform3f(getUniformLocation(name), value.x, value.y, value.z);
    }

    public void setUniform(String name, Vector4f value) {
        GL20.glUniform4f(getUniformLocation(name), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String name, Matrix4f value) {
        GL20.glUniformMatrix4fv(getUniformLocation(name), false, value.get(new float[16]));
    }

    public void setUniform(String name, boolean value) {
        GL20.glUniform1i(getUniformLocation(name), value ? 1 : 0);
    }

    public void destroy() {
        GL20.glDetachShader(programId, vertexId);
        GL20.glDetachShader(programId, fragmentId);

        if (geometryFile != null) {
            GL20.glDetachShader(programId, geometryId);
            GL20.glDeleteShader(geometryId);
        }

        GL20.glDeleteShader(vertexId);
        GL20.glDeleteShader(fragmentId);

        GL20.glDeleteProgram(programId);
    }

    public void createShader() {
        programId = GL20.glCreateProgram();

        String vertexSource = vertexContent, fragmentSource = fragmentContent;
        for (int i = libraries.size() - 1; i >= 0; i--) {
            ShaderLibrary library = libraries.get(i);
            if (library.getType() == ShaderLibrary.ShaderType.VERTEX || library.getType() == ShaderLibrary.ShaderType.ALL) {
                vertexSource = library.inject(vertexSource);
            }
            if (library.getType() == ShaderLibrary.ShaderType.FRAGMENT || library.getType() == ShaderLibrary.ShaderType.ALL) {
                fragmentSource = library.inject(fragmentSource);
            }
        }

        vertexId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexId, vertexSource);
        GL20.glCompileShader(vertexId);
        if (GL20.glGetShaderi(vertexId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed to compile vertex shader: " + GL20.glGetShaderInfoLog(vertexId));
            return;
        }

        fragmentId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentId, fragmentSource);
        GL20.glCompileShader(fragmentId);
        if (GL20.glGetShaderi(fragmentId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed to compile fragment shader: " + GL20.glGetShaderInfoLog(fragmentId));
            return;
        }

        GL20.glAttachShader(programId, vertexId);
        GL20.glAttachShader(programId, fragmentId);
        GL20.glLinkProgram(programId);
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed to link program: " + GL20.glGetProgramInfoLog(programId));
            return;
        }

        boolean hasGeometry = geometryFile != null;
        if (hasGeometry) {
            String geometrySource = geometryContent;
            for (int i = libraries.size() - 1; i >= 0; i--) {
                ShaderLibrary library = libraries.get(i);
                if (library.getType() == ShaderLibrary.ShaderType.GEOMETRY || library.getType() == ShaderLibrary.ShaderType.ALL) {
                    geometrySource = library.inject(geometrySource);
                }
            }

            geometryId = GL20.glCreateShader(GL33.GL_GEOMETRY_SHADER);
            GL20.glShaderSource(geometryId, geometrySource);
            GL20.glCompileShader(geometryId);
            if (GL20.glGetShaderi(geometryId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
                System.err.println("Failed to compile geometry shader: " + GL20.glGetShaderInfoLog(geometryId));
                return;
            }

            GL20.glAttachShader(programId, geometryId);
            GL20.glLinkProgram(programId);
            if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
                System.err.println("Failed to link program: " + GL20.glGetProgramInfoLog(programId));
                return;
            }
        }

        GL20.glDeleteShader(vertexId);
        GL20.glDeleteShader(fragmentId);

        validated = false;
        validate();
    }

}
