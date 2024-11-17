struct Material {
    vec4 color;
    float shineDamper;
    float reflectivity;

    sampler2D tex;

    bool hasNormalMap;
    sampler2D normalMap;
    float normalMapStrength;
};

struct Light {
    vec3 position;
    vec3 color;
    float intensity;
    float attenuation;
};