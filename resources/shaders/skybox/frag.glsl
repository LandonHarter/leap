#version 460 core

in vec3 vertex_textureCoord;

uniform samplerCube skybox;

out vec4 fragColor;

void main() {
    fragColor = texture(skybox, vertex_textureCoord);
}