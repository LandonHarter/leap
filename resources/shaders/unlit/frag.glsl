#version 330 core

in vec3 vertex_position;
in vec2 vertex_textureCoord;
in vec3 vertex_normal;

uniform sampler2D tex;
uniform vec3 color;

out vec4 outColor;

void main() {
    outColor = texture(tex, vertex_textureCoord) * vec4(color, 1.0f);
}