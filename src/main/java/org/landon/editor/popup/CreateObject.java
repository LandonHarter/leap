package org.landon.editor.popup;

import imgui.ImGui;

public class CreateObject extends Popup {

    @Override
    public void render(Object ...args) {
        ImGui.text("Create Object");
    }

}
