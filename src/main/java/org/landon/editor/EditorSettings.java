package org.landon.editor;

import org.joml.Vector3f;
import org.landon.project.Project;
import org.landon.serialization.Serializer;
import org.landon.util.FileUtil;

import java.io.File;

public class EditorSettings {

    private final Vector3f cameraPosition = new Vector3f(0, 0, 0);
    private final Vector3f cameraRotation = new Vector3f(0, 0, 0);
    private float cameraMoveSpeed = 80;
    private float cameraSensitivity = 8;

    private boolean renderGrid = true;

    public void save() {
        File file = new File(Project.getRootDirectory(), "editor_settings.json");
        String json = Serializer.toJson(this);
        FileUtil.writeFile(file, json);
    }

    public static EditorSettings load() {
        File file = new File(Project.getRootDirectory(), "editor_settings.json");
        if (file.exists()) {
            String json = FileUtil.readFile(file);
            return Serializer.fromJson(json, EditorSettings.class);
        }
        return new EditorSettings();
    }

    public Vector3f getCameraPosition() {
        return cameraPosition;
    }

    public void setCameraPosition(Vector3f cameraPosition) {
        this.cameraPosition.set(cameraPosition);
    }

    public Vector3f getCameraRotation() {
        return cameraRotation;
    }

    public void setCameraRotation(Vector3f cameraRotation) {
        this.cameraRotation.set(cameraRotation);
    }

    public boolean shouldRenderGrid() {
        return renderGrid;
    }

    public void setRenderGrid(boolean renderGrid) {
        this.renderGrid = renderGrid;
    }

    public float getCameraMoveSpeed() {
        return cameraMoveSpeed;
    }

    public void setCameraMoveSpeed(float cameraMoveSpeed) {
        this.cameraMoveSpeed = cameraMoveSpeed;
    }

    public float getCameraSensitivity() {
        return cameraSensitivity;
    }

    public void setCameraSensitivity(float cameraSensitivity) {
        this.cameraSensitivity = cameraSensitivity;
    }

}
