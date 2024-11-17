vec3 calculateLight(Light light, Material material, vec3 normal, vec3 position, vec3 cameraPosition) {
    vec3 lightDirection = normalize(light.position - position);
    vec3 cameraDirection = normalize(cameraPosition - position);
    vec3 halfDirection = normalize(lightDirection + cameraDirection);

    vec3 reflectedLightDirection = reflect(-lightDirection, normal);
    float specularFactor = max(dot(reflectedLightDirection, cameraDirection), 0);
    float dampedFactor = pow(specularFactor, material.shineDamper);
    vec3 specular = dampedFactor * material.reflectivity * light.color;

    float brightness = max(dot(normal, lightDirection), 0);
    vec3 diffuse = brightness * light.color;

    float distance = length(light.position - position);
    float attenuation = 1.0 / (1.0 + light.attenuation * distance * distance);
    diffuse *= attenuation;
    specular *= attenuation;

    return (diffuse + specular) * light.color * light.intensity * attenuation;
}

vec3 calculateNormal(sampler2D normalMap, vec2 textureCoord, vec3 vnormal, mat3 tbn) {
    vec3 normalTexture = texture(normalMap, textureCoord).xyz * 2.0 - 1.0;
    vec3 NewNormal = tbn * normalTexture;
    return mix(vnormal, NewNormal, 1f);
}