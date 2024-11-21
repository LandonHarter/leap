package org.landon.editor.popup;

import imgui.ImGui;

import java.util.function.Consumer;

public class ClipboardPopup extends Popup {

    private Object payload;
    private Object clipboard;
    private Consumer<Object> onPaste;

    @Override
    public void render() {
        if (ImGui.menuItem("Copy")) {
            clipboard = payload;
            close();
        }
        if (ImGui.menuItem("Paste")) {
            onPaste.accept(null);
            close();
        }
    }

    public void setOnPaste(Consumer<Object> onPaste) {
        this.onPaste = onPaste;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public <T> T getClipboard(Class<T> type) {
        if (clipboard == null) return null;
        if (type.isAssignableFrom(clipboard.getClass())) {
            return type.cast(clipboard);
        }

        return null;
    }

}
