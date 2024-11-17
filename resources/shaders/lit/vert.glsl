#version 330 core

layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec2 vertexTextureCoordinate;
layout(location = 2) in vec3 vertexNormal;

out DATA {
    vec3 vertex_position;
    vec2 vertex_textureCoord;
    vec3 vertex_normal;
} data;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    data.vertex_position = vertexPosition;
    data.vertex_textureCoord = vertexTextureCoordinate;
    data.vertex_normal = vertexNormal;

    gl_Position = model * vec4(vertexPosition, 1.0f);
}