#version 330 core

in vec3 vertexPosition;
in vec2 vertexTextureCoord;
in vec3 vertexNormal;

uniform Material material;
uniform Light[16] lights;

out vec4 outColor;

void main() {
    outColor = texture(material.tex, vertexTextureCoord) * material.color;

    vec3 normal = normalize(vertexNormal);
    if (material.hasNormalMap) {
        normal = calculateNormal(material.normalMap, vertexTextureCoord, vertexNormal);
    }

    vec3 totalLight = vec3(0);
    for (int i = 0; i < lights.length(); i++) {
        Light light = lights[i];
        if (light.intensity == 0) continue;

        vec3 l = calculateLight(light, material, normal, vertexPosition);
        totalLight += l;
    }
    totalLight = max(totalLight, vec3(0.1f));
    outColor *= vec4(totalLight, 1.0f);
}