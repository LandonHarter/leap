#version 330 core

in vec3 vertex_position;
in vec2 vertex_textureCoord;

uniform sampler2D tex;

out vec4 outColor;

void main() {
    outColor = vec4(vec3(1) - texture(tex, vertex_textureCoord).rgb, 1);
}