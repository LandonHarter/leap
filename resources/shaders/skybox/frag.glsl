#version 460 core

in vec3 vertex_position;

uniform samplerCube cubemap;

out vec4 fragColor;

void main() {
    fragColor = texture(cubemap, vertex_position);
}