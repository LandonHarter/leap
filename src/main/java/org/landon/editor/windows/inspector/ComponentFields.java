package org.landon.editor.windows.inspector;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.landon.annoations.HideField;
import org.landon.components.Component;
import org.landon.editor.Icons;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ComponentFields {

    public static void render(Component c) {
        if (ImGui.checkbox("##enable-" + c.getUUID(), c.isEnabled())) {
            c.setEnabled(!c.isEnabled());
        }
        ImGui.sameLine();
        ImGui.image(Icons.getIcon(c.getClass().getSimpleName()), 20, 20);
        ImGui.sameLine();
        if (ImGui.treeNodeEx(c.getUUID(), ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.SpanAvailWidth, c.getName())) {
            ImGui.indent(3);
            Field[] fields = c.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isTransient(field.getModifiers())) continue;
                if (Modifier.isStatic(field.getModifiers())) continue;
                if (Modifier.isFinal(field.getModifiers())) continue;
                if (field.isAnnotationPresent(HideField.class)) continue;

                field.setAccessible(true);

                try {
                    renderField(field, c);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            ImGui.unindent(3);
            ImGui.treePop();
        }
    }

    private static void renderField(Field field, Component c) throws IllegalAccessException {
        if (field.getType() == boolean.class) booleanField(field, c);
        else if (field.getType() == int.class) intField(field, c);
        else if (field.getType() == float.class) floatField(field, c);
        else if (field.getType() == String.class) stringField(field, c);
        else if (field.getType() == Vector2f.class) vector2Field(field, c);
        else if (field.getType() == Vector3f.class) vector3Field(field, c);
    }

    private static void booleanField(Field field, Component c) throws IllegalAccessException {
        if (ImGui.checkbox(formatFieldName(field.getName()), field.getBoolean(c))) {
            field.setBoolean(c, !field.getBoolean(c));
        }
    }

    private static void intField(Field field, Component c) throws IllegalAccessException {
        int[] value = new int[] { field.getInt(c) };
        if (ImGui.dragInt(formatFieldName(field.getName()), value)) {
            field.setInt(c, value[0]);
        }
    }

    private static void floatField(Field field, Component c) throws IllegalAccessException {
        float[] value = new float[] { field.getFloat(c) };
        if (ImGui.dragFloat(formatFieldName(field.getName()), value)) {
            field.setFloat(c, value[0]);
        }
    }

    private static void stringField(Field field, Component c) throws IllegalAccessException {
        String value = (String) field.get(c);
        ImString imValue = new ImString(value);
        if (ImGui.inputText(formatFieldName(field.getName()), imValue)) {
            field.set(c, imValue.get());
        }
    }

    private static void vector2Field(Field field, Component c) throws IllegalAccessException {
        float[] value = (float[]) field.get(c);
        if (ImGui.dragFloat2(formatFieldName(field.getName()), value)) {
            field.set(c, value);
        }
    }

    private static void vector3Field(Field field, Component c) throws IllegalAccessException {
        float[] value = (float[]) field.get(c);
        if (ImGui.dragFloat3(formatFieldName(field.getName()), value)) {
            field.set(c, value);
        }
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
