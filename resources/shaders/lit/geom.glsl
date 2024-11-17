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

uniform vec3 cameraPosition;

out vec3 vertexPosition;
out vec2 vertexTextureCoord;
out vec3 vertexNormal;
out mat3 TBN;

void main() {
    vec3 edge0 = gl_in[1].gl_Position.xyz - gl_in[0].gl_Position.xyz;
    vec3 edge1 = gl_in[2].gl_Position.xyz - gl_in[0].gl_Position.xyz;
    vec2 deltaUV0 = data[1].vertex_textureCoord - data[0].vertex_textureCoord;
    vec2 deltaUV1 = data[2].vertex_textureCoord - data[0].vertex_textureCoord;
    float invDet = 1.0f / (deltaUV0.x * deltaUV1.y - deltaUV1.x * deltaUV0.y);
    vec3 tangent = normalize((model * vec4(vec3(invDet * (deltaUV1.y * edge0 - deltaUV0.y * edge1)), 0.0)).xyz);
    vec3 normal = normalize((model * vec4(data[0].vertex_normal, 0.0f)).xyz);
    tangent = normalize(tangent - dot(tangent, normal) * normal);
    vec3 bitangent = cross(normal, tangent);
    TBN = mat3(tangent, bitangent, normal);

    gl_Position = projection * view * gl_in[0].gl_Position;
    vertexPosition = gl_in[0].gl_Position.xyz;
    vertexTextureCoord = data[0].vertex_textureCoord;
    vertexNormal = (model * vec4(data[0].vertex_normal, 0.0f)).xyz;
    EmitVertex();

    gl_Position = projection * view * gl_in[1].gl_Position;
    vertexPosition = gl_in[1].gl_Position.xyz;
    vertexTextureCoord = data[1].vertex_textureCoord;
    vertexNormal = (model * vec4(data[1].vertex_normal, 0.0f)).xyz;
    EmitVertex();

    gl_Position = projection * view * gl_in[2].gl_Position;
    vertexPosition = gl_in[2].gl_Position.xyz;
    vertexTextureCoord = data[2].vertex_textureCoord;
    vertexNormal = (model * vec4(data[2].vertex_normal, 0.0f)).xyz;
    EmitVertex();

    EndPrimitive();
}