#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in DATA {
    vec3 vertex_position;
    vec2 vertex_textureCoord;
    vec3 vertex_normal;
} data[];

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 vertexPosition;
out vec2 vertexTextureCoord;
out vec3 vertexNormal;

void main() {
    gl_Position = projection * view * gl_in[0].gl_Position;
    vertexPosition = (model * vec4(data[0].vertex_position, 1.0f)).xyz;
    vertexTextureCoord = data[0].vertex_textureCoord;
    vertexNormal = (model * vec4(data[0].vertex_normal, 0.0f)).xyz;
    EmitVertex();

    gl_Position = projection * view * gl_in[1].gl_Position;
    vertexPosition = (model * vec4(data[1].vertex_position, 1.0f)).xyz;
    vertexTextureCoord = data[1].vertex_textureCoord;
    vertexNormal = (model * vec4(data[1].vertex_normal, 0.0f)).xyz;
    EmitVertex();

    gl_Position = projection * view * gl_in[2].gl_Position;
    vertexPosition = (model * vec4(data[2].vertex_position, 1.0f)).xyz;
    vertexTextureCoord = data[2].vertex_textureCoord;
    vertexNormal = (model * vec4(data[2].vertex_normal, 0.0f)).xyz;
    EmitVertex();

    EndPrimitive();
}