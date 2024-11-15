package org.landon.editor.popup;

import imgui.ImGui;
import org.landon.editor.windows.logger.Log;

public class LogPopup extends Popup {

    private Log log;

    @Override
    public void renderBase() {
        ImGui.setNextWindowSize(500, 350);
        if (ImGui.beginPopup(id)) {
            render();
            ImGui.endPopup();
        }
    }

    @Override
    public void render() {
        if (log == null) return;

        ImGui.text("Log Type: " + log.getType());
        ImGui.textWrapped("Message: " + log.getMessage());
    }

    public void setLog(Log log) {
        this.log = log;
    }

}
