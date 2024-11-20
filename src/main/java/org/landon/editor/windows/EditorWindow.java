package org.landon.editor.windows;

import imgui.ImGui;

import java.util.HashMap;

public class EditorWindow {

    private static final HashMap<String, EditorWindow> windows = new HashMap<>();

    protected boolean open = false;
    protected String title;

    protected EditorWindow(String title) {
        windows.put(title, this);
        this.title = title;
    }

    public void renderBase() {
        if (open) {
            ImGui.begin(title);
            render();
            ImGui.end();
        }
    }

    public void render() {}

    public void open() {
        open = true;
    }

    public void close() {
        open = false;
    }

    public static void renderAll() {
        for (EditorWindow window : windows.values()) {
            window.renderBase();
        }
    }

    public static void openWindow(String title) {
        windows.get(title).open();
    }

    public static void closeWindow(String title) {
        windows.get(title).close();
    }

    public static void createInstances() {
        new Preferences();
    }

}
