package org.landon.editor.popup;

import imgui.ImGui;

import java.util.UUID;

public class Popup {

    private final String id;
    private boolean open;

    public Popup() {
        this.id = UUID.randomUUID().toString();
    }

    public void renderBase(Object ...args) {
        if (ImGui.beginPopup(id)) {
            render(args);
            ImGui.endPopup();
        }
    }

    public void render(Object ...args) {}

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
        ImGui.openPopup(id);
    }

}
