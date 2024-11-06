#version 460 core

layout(location = 0) in vec3 vertexPosition;

out vec3 vertex_position;
out vec3 vertex_textureCoord;

uniform mat4 view;
uniform mat4 projection;

void main() {
    vertex_textureCoord = vertexPosition;
    gl_Position = projection * view * vec4(vertexPosition, 1.0);
}