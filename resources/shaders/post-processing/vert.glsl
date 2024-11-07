#version 330 core

layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec2 vertexTextureCoordinate;

out vec3 vertex_position;
out vec2 vertex_textureCoord;

void main() {
    vertex_position = vertexPosition;
    vertex_textureCoord = vertexTextureCoordinate;
    gl_Position = vec4(vertexPosition, 1.0f);
}