package org.landon.editor.windows.inspector;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.landon.annoations.ExecuteGui;
import org.landon.annoations.HideField;
import org.landon.annoations.RangeFloat;
import org.landon.annoations.RangeInt;
import org.landon.components.Component;
import org.landon.editor.Icons;
import org.landon.editor.popup.FileChooser;
import org.landon.editor.windows.logger.Logger;
import org.landon.graphics.material.Material;
import org.landon.graphics.material.Texture;
import org.landon.project.ProjectFiles;
import org.landon.serialization.types.LeapEnum;
import org.landon.serialization.types.LeapFloat;
import org.landon.util.FileUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ComponentFields {

    private static FileChooser fileChooser = new FileChooser(new String[] {}, file -> {});

    public static void render(Component c) {
        ImVec2 cursorPos = ImGui.getCursorPos();
        if (c.canDisable()) {
            if (ImGui.checkbox("##enable-" + c.getUUID(), c.isEnabled())) {
                c.setEnabled(!c.isEnabled());
            }
            ImGui.sameLine();
        }
        ImGui.setCursorPosY(cursorPos.y + 3);
        ImGui.image(Icons.getIcon(c.getClass().getSimpleName()), 20, 20);
        ImGui.sameLine();
        ImGui.setCursorPosY(cursorPos.y);
        if (ImGui.treeNodeEx(c.getUUID(), ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.SpanAvailWidth, c.getName())) {
            ImGui.indent(6);
            ImGui.setCursorPosY(ImGui.getCursorPosY() + 4);
            Field[] fields = c.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isTransient(field.getModifiers())) continue;
                if (Modifier.isStatic(field.getModifiers())) continue;

                field.setAccessible(true);
                try {
                    renderField(field, c);
                    ImGui.setCursorPosY(ImGui.getCursorPosY() + 2);
                } catch (IllegalAccessException e) {
                    Logger.error(e);
                }
            }

            ImGui.unindent(6);
            ImGui.treePop();
        }
    }

    private static void renderField(Field field, Component c) throws IllegalAccessException {
        if (!field.isAnnotationPresent(HideField.class)) {
            if (field.getType() == boolean.class) booleanField(field, c);
            else if (field.getType() == int.class) intField(field, c);
            else if (field.getType() == LeapFloat.class) floatField(field, c);
            else if (field.getType() == String.class) stringField(field, c);
            else if (field.getType() == Vector2f.class) vector2Field(field, c);
            else if (field.getType() == Vector3f.class) vector3Field(field, c);
            else if (field.getType() == Material.class) materialField(field, c);
            else if (field.getType() == LeapEnum.class) enumField(field, c);
            else if (field.getType() == Vector4f.class) colorField(field, c);
        }

        ExecuteGui executeGui = field.getAnnotation(ExecuteGui.class);
        if (executeGui != null) {
            c.executeGui(executeGui.value());
        }
    }

    private static void booleanField(Field field, Component c) throws IllegalAccessException {
        if (ImGui.checkbox(formatFieldName(field.getName()), field.getBoolean(c))) {
            field.setBoolean(c, !field.getBoolean(c));
            c.variableUpdated(field);
        }
    }

    private static void intField(Field field, Component c) throws IllegalAccessException {
        int[] value = new int[] { field.getInt(c) };
        RangeInt range = field.getAnnotation(RangeInt.class);

        boolean changed = range == null ? ImGui.dragInt(formatFieldName(field.getName()), value) : ImGui.sliderInt(formatFieldName(field.getName()), value, range.min(), range.max());
        if (changed) {
            field.setInt(c, value[0]);
            c.variableUpdated(field);
        }
    }

    private static void floatField(Field field, Component c) throws IllegalAccessException {
        LeapFloat value = (LeapFloat) field.get(c);
        float[] floatValue = new float[] { value.getValue() };
        RangeFloat range = field.getAnnotation(RangeFloat.class);

        boolean changed = range == null ? ImGui.dragFloat(formatFieldName(field.getName()), floatValue) : ImGui.sliderFloat(formatFieldName(field.getName()), floatValue, range.min(), range.max());
        if (changed) {
            value.setValue(floatValue[0]);
            c.variableUpdated(field);
        }
    }

    private static void stringField(Field field, Component c) throws IllegalAccessException {
        String value = (String) field.get(c);
        ImString imValue = new ImString(value);
        if (ImGui.inputText(formatFieldName(field.getName()), imValue)) {
            field.set(c, imValue.get());
            c.variableUpdated(field);
        }
    }

    private static void vector2Field(Field field, Component c) throws IllegalAccessException {
        float[] value = (float[]) field.get(c);
        if (ImGui.dragFloat2(formatFieldName(field.getName()), value)) {
            field.set(c, value);
            c.variableUpdated(field);
        }
    }

    private static void vector3Field(Field field, Component c) throws IllegalAccessException {
        float[] value = (float[]) field.get(c);
        if (ImGui.dragFloat3(formatFieldName(field.getName()), value)) {
            field.set(c, value);
            c.variableUpdated(field);
        }
    }

    private static void materialField(Field field, Component c) throws IllegalAccessException {
        Material material = (Material) field.get(c);

        float[] color = toFloatArray(material.getColor());
        if (ImGui.colorEdit4(formatFieldName(field.getName()), color, ImGuiColorEditFlags.AlphaBar)) {
            material.setColor(color[0], color[1], color[2], color[3]);
            c.variableUpdated(field);
        }

        ImGui.setCursorPosY(ImGui.getCursorPosY() + 2);

        float[] shine = new float[] { material.getShineDamper() };
        if (ImGui.dragFloat("Shine Damper", shine)) {
            material.setShineDamper(shine[0]);
            c.variableUpdated(field);
        }

        ImGui.setCursorPosY(ImGui.getCursorPosY() + 2);

        float[] reflectivity = new float[] { material.getReflectivity() };
        if (ImGui.dragFloat("Reflectivity", reflectivity)) {
            material.setReflectivity(reflectivity[0]);
            c.variableUpdated(field);
        }

        ImGui.setCursorPosY(ImGui.getCursorPosY() + 2);

        if (ImGui.button("Choose##texture")) {
            fileChooser.setExtensions(ProjectFiles.IMAGE_EXTENSIONS);
            fileChooser.setAllowNone(false);
            fileChooser.setOnFileSelected(file -> {
                material.setTexture(new Texture(file.getPath()));
                c.variableUpdated(field);
            });
            fileChooser.setSelectedFile(material.getTexture().getFile());
            fileChooser.open();
        }
        ImGui.sameLine();
        if (ImGui.treeNodeEx("Texture (" + material.getTexture().getName() + ")", ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.SpanAvailWidth)) {
            if (ImGui.beginDragDropTarget()) {
                File file = ImGui.acceptDragDropPayload(File.class);
                if (file != null && FileUtil.isExtension(file, ProjectFiles.IMAGE_EXTENSIONS)) {
                    material.setTexture(new Texture(file.getPath()));
                    c.variableUpdated(field);
                }

                ImGui.endDragDropTarget();
            }

            ImGui.treePop();
        }

        ImGui.setCursorPosY(ImGui.getCursorPosY() + 2);

        Texture normalMap = material.getNormalMap();
        if (ImGui.button("Choose##normal")) {
            fileChooser.setExtensions(ProjectFiles.IMAGE_EXTENSIONS);
            fileChooser.setAllowNone(true);
            fileChooser.setOnFileSelected(file -> {
                if (file == null) {
                    material.setNormalMap(null);
                } else {
                    material.setNormalMap(new Texture(file.getPath()));
                }
                c.variableUpdated(field);
            });
            fileChooser.setSelectedFile(material.getNormalMap() != null ? material.getNormalMap().getFile() : null);
            fileChooser.open();
        }
        ImGui.sameLine();
        if (ImGui.treeNodeEx("Normal Map (" + (normalMap != null ? normalMap.getName() : "None") + ")", ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.SpanAvailWidth)) {
            if (ImGui.beginDragDropTarget()) {
                File file = ImGui.acceptDragDropPayload(File.class);
                if (file != null && FileUtil.isExtension(file, ProjectFiles.IMAGE_EXTENSIONS)) {
                    material.setNormalMap(new Texture(file.getPath()));
                    c.variableUpdated(field);
                }

                ImGui.endDragDropTarget();
            }

            ImGui.treePop();
        }
        if (normalMap != null) {
            ImGui.setCursorPosY(ImGui.getCursorPosY() + 2);

            float[] normalMapScale = new float[] { material.getNormalMapStrength() };
            if (ImGui.sliderFloat("Normal Map Strength", normalMapScale, 0, 1)) {
                material.setNormalMapStrength(normalMapScale[0]);
                c.variableUpdated(field);
            }
        }
    }

    private static void enumField(Field field, Component c) throws IllegalAccessException {
        LeapEnum<?> value = (LeapEnum<?>) field.get(c);
        Class<?> type = value.getValue().getClass();
        String[] values = new String[type.getEnumConstants().length];
        for (int i = 0; i < values.length; i++) {
            values[i] = type.getEnumConstants()[i].toString();
            values[i] = values[i].substring(0, 1).toUpperCase() + values[i].substring(1).toLowerCase();
        }

        ImInt selected = new ImInt(value.getValue().ordinal());
        if (ImGui.combo(formatFieldName(field.getName()), selected, values, values.length)) {
            value.setValue(selected.get());
            c.variableUpdated(field);
        }
    }

    private static void colorField(Field field, Component c) throws IllegalAccessException {
        Vector4f value = (Vector4f) field.get(c);
        float[] color = toFloatArray(value);
        if (ImGui.colorEdit4(formatFieldName(field.getName()), color)) {
            value.set(color[0], color[1], color[2], color[3]);
            c.variableUpdated(field);
        }
    }

    private static float[] toFloatArray(Vector3f vector) {
        return new float[] { vector.x, vector.y, vector.z };
    }

    private static float[] toFloatArray(Vector4f vector) {
        return new float[] { vector.x, vector.y, vector.z, vector.w };
    }

    private static String formatFieldName(String name) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (i == 0) {
                formatted.append(Character.toUpperCase(c));
            } else if (Character.isUpperCase(c)) {
                formatted.append(" ").append(c);
            } else {
                formatted.append(c);
            }
        }

        return formatted.toString();
    }

}
