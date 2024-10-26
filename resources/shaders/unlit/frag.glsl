#version 330 core

in vec3 vertex_position;
in vec2 vertex_textureCoord;
in vec3 vertex_normal;

in vec4 worldPosition;
in mat4 viewMatrix;

out vec4 outColor;

void main() {
    outColor = vec4(vertex_normal, 1.0f);
}