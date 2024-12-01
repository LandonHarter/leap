#version 330 core

in vec3 vertex_position;
in vec2 vertex_textureCoord;

uniform Material material;

out vec4 outColor;

void main() {
    outColor = texture(material.tex, vertex_textureCoord) * material.color;
}