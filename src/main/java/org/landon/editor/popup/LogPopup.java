package org.landon.editor.popup;

import imgui.ImGui;
import org.landon.editor.windows.logger.Log;

public class LogPopup extends Popup {

    private Log log;

    @Override
    public void render() {
        if (log == null) return;

        ImGui.text("Log Type: " + log.getType());
        ImGui.text("Message: " + log.getMessage());
    }

    public void setLog(Log log) {
        this.log = log;
    }

}
