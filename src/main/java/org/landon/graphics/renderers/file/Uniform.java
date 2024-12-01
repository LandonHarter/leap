package org.landon.graphics.renderers.file;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class Uniform<T> {

    private String name;
    private T value;

    private final static List<String> DEFAULT_UNIFORMS = List.of("model", "view", "projection", "cameraPosition", "material", "lights");

    public Uniform(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static Uniform<?> createUniform(String name, String type) {
        return switch (type) {
            case "int" -> new Uniform<Integer>(name, 0);
            case "float" -> new Uniform<Float>(name, 0.0f);
            case "vec2" -> new Uniform<Vector2f>(name, new Vector2f());
            case "vec3" -> new Uniform<Vector3f>(name, new Vector3f());
            case "vec4" -> new Uniform<Vector4f>(name, new Vector4f());
            default -> throw new IllegalArgumentException("Unknown uniform type: " + type);
        };
    }

}
