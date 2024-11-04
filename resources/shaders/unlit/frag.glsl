#version 330 core

in vec3 vertex_position;
in vec2 vertex_textureCoord;
in vec3 vertex_normal;

uniform bool useTexture;
uniform sampler2D tex;
uniform vec3 color;

out vec4 outColor;

void main() {
    outColor = vec4(color, 1.0f);
    if (useTexture) {
        outColor = texture(tex, vertex_textureCoord);
    }
}