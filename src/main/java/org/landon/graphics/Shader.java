package org.landon.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.landon.util.FileUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.File;

public class Shader {

    private String vertexContent, fragmentContent;
    private File vertexFile, fragmentFile;

    private int vertexId, fragmentId, programId;
    private boolean validated;

    private static int currentProgram = 0;

    public Shader(String vertexPath, String fragmentPath) {
        vertexFile = new File(vertexPath);
        fragmentFile = new File(fragmentPath);
        vertexContent = FileUtil.readFile(vertexFile);
        fragmentContent = FileUtil.readFile(fragmentFile);

        createShader();
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
        return GL20.glGetUniformLocation(programId, name);
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

    public void destroy() {
        GL20.glDetachShader(programId, vertexId);
        GL20.glDetachShader(programId, fragmentId);
        GL20.glDeleteShader(vertexId);
        GL20.glDeleteShader(fragmentId);
        GL20.glDeleteProgram(programId);
    }

    private void createShader() {
        programId = GL20.glCreateProgram();

        vertexId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexId, vertexContent);
        GL20.glCompileShader(vertexId);
        if (GL20.glGetShaderi(vertexId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed to compile vertex shader: " + GL20.glGetShaderInfoLog(vertexId));
            return;
        }

        fragmentId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentId, fragmentContent);
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

        GL20.glDeleteShader(vertexId);
        GL20.glDeleteShader(fragmentId);

        validated = false;
        validate();
    }

}
