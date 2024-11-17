#version 330 core

in vec3 vertexPosition;
in vec2 vertexTextureCoord;
in vec3 vertexNormal;
in mat3 TBN;

uniform Material material;
uniform Light[16] lights;
uniform vec3 cameraPosition;

out vec4 outColor;

void main() {
    outColor = texture(material.tex, vertexTextureCoord) * material.color;

    vec3 normal = normalize(vertexNormal);
    if (material.hasNormalMap) {
        normal = calculateNormal(material.normalMap, vertexTextureCoord, vertexNormal, TBN);
    }

    vec3 totalLight = vec3(0);
    for (int i = 0; i < lights.length(); i++) {
        Light light = lights[i];
        if (light.intensity == 0) continue;

        vec3 l = calculateLight(light, material, normal, vertexPosition, cameraPosition);
        totalLight += l;
    }
    totalLight = max(totalLight, vec3(0.1f));
    outColor *= vec4(totalLight, 1.0f);
}