#version 330 core

in vec3 vertexPosition;
in vec3 tangentPosition;
in vec2 vertexTextureCoord;
in vec3 vertexNormal;
in vec3 tangentCameraPosition;
in mat3 TBN;

uniform Material material;
uniform Light[1] lights;
uniform vec3 cameraPosition;

out vec4 outColor;

float D(float alpha, vec3 N, vec3 H) {
    float numerator = pow(alpha, 2.0);
    float NdotH = max(dot(N, H), 0);
    float denominator = PI * pow(pow(NdotH, 2.0) * (pow(alpha, 2.0) - 1.0) + 1.0, 2.0);
    denominator = max(denominator, 0.000001);
    return numerator / denominator;
}

float G1(float alpha, vec3 N, vec3 X) {
    float numerator = max(dot(N, X), 0.0);

    float k = alpha / 2.0;
    float denominator = max(dot(N, X), 0.0) * (1.0 - k) + k;
    denominator = max(denominator, 0.000001);

    return numerator / denominator;
}

float G(float alpha, vec3 N, vec3 V, vec3 L) {
    return G1(alpha, N, V) * G1(alpha, N, L);
}

vec3 F(vec3 F0, vec3 V, vec3 H) {
    return F0 + (vec3(1.0) - F0) * pow(1.0 - dot(V, H), 5.0);
}

vec2 displaceCoords() {
    if (!material.hasDisplacementMap) return vertexTextureCoord;

    vec3 viewDirection = normalize(tangentCameraPosition - tangentPosition);
    float heightScale = material.displacementMapStrength / 20.0f;
    const float minLayers = 8.0f;
    const float maxLayers = 64.0f;
    float numLayers = mix(maxLayers, minLayers, abs(dot(vec3(0, 0, 1), viewDirection)));
    float layerDepth = 1.0f / numLayers;
    float currentLayerDepth = 0.0f;
    vec2 S = viewDirection.xy / viewDirection.z * heightScale;
    vec2 deltaUVS = S / numLayers;
    vec2 UVS = vertexTextureCoord;
    float currentDepthMapValue = 1.0f - texture(material.displacementMap, UVS).r;
    while (currentLayerDepth < currentDepthMapValue) {
        UVS -= deltaUVS;
        currentDepthMapValue = 1.0f - texture(material.displacementMap, UVS).r;
        currentLayerDepth += layerDepth;
    }
    vec2 prevTexCoords = UVS + deltaUVS;
    float afterDepth = currentDepthMapValue - currentLayerDepth;
    float beforeDepth = 1.0f - texture(material.displacementMap, prevTexCoords).r - currentLayerDepth + layerDepth;
    float weight = afterDepth / (afterDepth - beforeDepth);
    UVS = prevTexCoords * weight + UVS * (1.0f - weight);

    if (UVS.x > 1.0f || UVS.y > 1.0f || UVS.x < 0.0f || UVS.y < 0.0f) discard;

    return UVS;
}

vec3 calculateNormal(vec2 texCoords) {
    if (!material.hasNormalMap) return vertexNormal;
    vec3 newNormal = texture(material.normalMap, texCoords).rgb * 2.0 - 1.0;
    newNormal = normalize(TBN * newNormal);
    return mix(vertexNormal, newNormal, material.normalMapStrength);
}

vec4 PBR(Light light, vec3 normal, vec3 col) {
    vec3 V = normalize(cameraPosition - vertexPosition);
    vec3 L = normalize(light.position - vertexPosition);
    vec3 H = normalize(V + L);

    vec3 Ks = F(vec3(material.baseReflectivity), V, H);
    vec3 Kd = (vec3(1.0f) - Ks) * (1.0 - material.metallic);

    vec3 lambert = col.rgb / vec3(PI);

    vec3 cookTorranceNumerator = D(material.alpha, normal, H) * G(material.alpha, normal, V, L) * F(vec3(material.baseReflectivity), V, H);
    float cookTorranceDenominator = 4.0 * max(dot(V, normal), 0.0) * max(dot(L, normal), 0.0);
    cookTorranceDenominator = max(cookTorranceDenominator, 0.000001);
    vec3 cookTorrance = cookTorranceNumerator / cookTorranceDenominator;

    vec3 BRDF = Kd * lambert + cookTorrance;
    vec3 outgoingLight = light.intensity * BRDF * light.color * max(dot(L, normal), 0.0);

    float distanceFromLight = length(light.position - vertexPosition);
    float attenuation = 1.f / (1.f + light.attenuation * distanceFromLight * 0.0075f * (distanceFromLight * distanceFromLight));
    outgoingLight *= attenuation;

    return vec4(outgoingLight, 1);
}

void main() {
    vec2 texCoords = displaceCoords();
    vec3 normal = normalize(calculateNormal(texCoords));
    outColor = texture(material.tex, texCoords) * material.color;

    vec3 totalLight = vec3(0);
    for (int i = 0; i < lights.length(); i++) {
        Light light = lights[i];
        if (light.intensity <= 0) continue;

        vec4 l = PBR(light, normal, outColor.rgb);
        totalLight += l.xyz;
    }
    totalLight = max(totalLight, vec3(0.1f));
    outColor *= vec4(totalLight, 1);
}