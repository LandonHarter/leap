#version 330 core

in vec3 vertex_position;
in vec2 vertex_textureCoord;
in vec3 vertex_normal;

uniform Material material;
uniform Light[4] lights;

out vec4 outColor;

void main() {
    outColor = vec4(material.color, 1.0f);
    if (material.useTexture) {
        outColor = texture(material.tex, vertex_textureCoord);
    }

    vec3 normal = normalize(vertex_normal);
    vec3 totalLight = vec3(0);
    for (int i = 0; i < lights.length(); i++) {
        Light light = lights[i];
        if (light.intensity == 0) continue;

        vec3 l = calculateLight(light, material, normal, vertex_position);
        totalLight += l;
    }
    // ambient light
    totalLight = max(totalLight, vec3(0.1f));

    outColor *= vec4(totalLight, 1.0f);
}