struct Material {
    vec4 color;
    float shineDamper;
    float reflectivity;
    float metallic;
    float alpha;
    float baseReflectivity;

    sampler2D tex;

    bool hasNormalMap;
    sampler2D normalMap;
    float normalMapStrength;

    bool hasSpecularMap;
    sampler2D specularMap;
    float specularMapStrength;

    bool hasDisplacementMap;
    sampler2D displacementMap;
    float displacementMapStrength;
};

struct Light {
    vec3 position;
    vec3 color;
    float intensity;
    float attenuation;
};