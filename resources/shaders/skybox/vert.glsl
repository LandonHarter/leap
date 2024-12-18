#version 330 core

layout(location = 0) in vec3 vertexPosition;

uniform mat4 view;
uniform mat4 projection;

out vec3 vertex_position;

void main() {
    vertex_position = vertexPosition;
    gl_Position = projection * view * vec4(vertexPosition, 1.0);
}