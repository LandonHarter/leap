#version 330 core

in vec3 localPosition;

uniform sampler2D skybox;

out vec4 fragColor;

const vec2 invAtan = vec2(0.1591, 0.3183);
vec2 sampleSphericalMap(vec3 v) {
    vec2 uv = vec2(atan(v.z, v.x), asin(v.y));
    uv *= invAtan;
    uv += 0.5;
    return uv;
}

void main() {
    vec2 uv = sampleSphericalMap(normalize(localPosition));
    vec3 color = texture(skybox, uv).rgb;
    color = 1 - exp(-color * 0.5);
    fragColor = vec4(color, 1.0);
}