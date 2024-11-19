#version 330 core

in vec3 vertex_position;
in vec2 vertex_textureCoord;

uniform sampler2D tex;
uniform vec4 color;

out vec4 outColor;

void main() {
    outColor = texture(tex, vertex_textureCoord) * color;
}