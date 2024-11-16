#version 330 core

layout(location = 0) in vec3 vertex_position;
layout(location = 2) in vec3 vertex_normal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform float outline;

void main() {
    gl_Position = projection * view * model * vec4(vertex_position, 1.0);
}