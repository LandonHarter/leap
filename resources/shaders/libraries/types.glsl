struct Material {
    vec3 color;
    float shineDamper;
    float reflectivity;

    bool useTexture;
    sampler2D tex;
};

struct Light {
    vec3 position;
    vec3 color;
    float intensity;
    float attenuation;
};