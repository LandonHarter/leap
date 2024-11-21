#version 330 core

in vec3 vertex_position;

uniform samplerCube skybox;

out vec4 fragColor;

void main() {
    fragColor = texture(skybox, vertex_position);
}