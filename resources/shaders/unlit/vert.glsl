#version 460 core

layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec2 vertexTextureCoordinate;
layout(location = 2) in vec3 vertexNormal;

out vec3 vertex_position;
out vec2 vertex_textureCoord;
out vec3 vertex_normal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertex_position = (model * vec4(vertexPosition, 1.0f)).xyz;
    vertex_textureCoord = vertexTextureCoordinate;
    vertex_normal = (model * vec4(vertexNormal, 0.0f)).xyz;
    gl_Position = projection * view * model * vec4(vertexPosition, 1.0f);
}