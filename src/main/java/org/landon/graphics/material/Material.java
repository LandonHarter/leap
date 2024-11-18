package org.landon.graphics.material;

import org.joml.Vector4f;

public class Material {

    private Texture texture;
    private Texture normalMap;
    private Texture specularMap;
    private Texture displacementMap;

    private Vector4f color;
    private float metallic = 1.5f;
    private float glossiness = 100;
    private float fresnel = 5;
    private float normalMapStrength = 1;
    private float displacementMapStrength = 0;
    private float specularMapStrength = 1;

    private transient boolean hasTransparency = false;

    public Material() {
        this.texture = new Texture("resources/textures/default.png");
        this.color = new Vector4f(1, 1, 1, 1);
        hasTransparency = false;
    }

    public Material(Texture texture) {
        this.texture = texture;
        this.color = new Vector4f(1, 1, 1, 1);
        hasTransparency = texture.isTransparent() || color.w < 1;
    }

    public Material(Texture texture, Vector4f color) {
        this.texture = texture;
        this.color = color;
        hasTransparency = texture.isTransparent() || color.w < 1;
    }

    public void checkTransparency() {
        hasTransparency = texture.isTransparent() || color.w < 1;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getNormalMap() {
        return normalMap;
    }

    public float getNormalMapStrength() {
        return normalMapStrength;
    }

    public void setNormalMapStrength(float normalMapStrength) {
        this.normalMapStrength = normalMapStrength;
    }

    public void setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
    }

    public Texture getSpecularMap() {
        return specularMap;
    }

    public float getSpecularMapStrength() {
        return specularMapStrength;
    }

    public void setSpecularMapStrength(float specularMapStrength) {
        this.specularMapStrength = specularMapStrength;
    }

    public void setSpecularMap(Texture specularMap) {
        this.specularMap = specularMap;
    }

    public Texture getDisplacementMap() {
        return displacementMap;
    }

    public float getDisplacementMapStrength() {
        return displacementMapStrength;
    }

    public void setDisplacementMapStrength(float displacementMapStrength) {
        this.displacementMapStrength = displacementMapStrength;
    }

    public void setDisplacementMap(Texture displacementMap) {
        this.displacementMap = displacementMap;
    }

    public float getMetallic() {
        return metallic;
    }

    public void setMetallic(float metallic) {
        this.metallic = metallic;
    }

    public float getGlossiness() {
        return glossiness;
    }

    public void setGlossiness(float glossiness) {
        this.glossiness = glossiness;
    }

    public float getFresnel() {
        return fresnel;
    }

    public void setFresnel(float fresnel) {
        this.fresnel = fresnel;
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public void setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
    }

}
