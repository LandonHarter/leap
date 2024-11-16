package org.landon.editor.windows.logger;

import imgui.ImColor;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.landon.editor.Icons;
import org.landon.editor.popup.LogPopup;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    private static List<Log> logs = new ArrayList<>();
    private static Log selectedLog;
    private static final int MAX_LOGS = 250;
    private static boolean scroll;

    private static LogPopup popup = new LogPopup();

    public static void render() {
        ImGui.begin("\uF075  Logger (" + logs.size() + ")###Logger", ImGuiWindowFlags.MenuBar);
        if (ImGui.beginMenuBar()) {
            if (ImGui.menuItem("Clear")) {
                clear();
            }

            ImGui.endMenuBar();
        }

        logs.forEach(Logger::renderLog);

        ImGui.end();
    }

    private static void renderLog(Log log) {
        ImDrawList dl = ImGui.getWindowDrawList();
        ImVec2 pos = ImGui.getCursorScreenPos();
        dl.addRectFilled(pos.x, pos.y, pos.x + ImGui.getContentRegionAvailX(), pos.y + 50.0f, selectedLog == log ? ImColor.rgb("#3D6FA4") : ImGui.getColorU32(1, 1, 1, 0), 8);
        if (ImGui.isMouseHoveringRect(pos.x, pos.y, pos.x + ImGui.getWindowWidth(), pos.y + 50.0f) && ImGui.isWindowFocused()) {
            if (ImGui.isMouseClicked(0, false)) {
                selectedLog = log;
            }
            if (ImGui.isMouseDoubleClicked(0)) {
                popup.setLog(log);
                popup.open();
            }
        }

        ImVec2 dest = new ImVec2();
        ImGui.calcTextSize(dest, log.getMessage());
        float halfHeight = pos.y + (50.0f / 2 - dest.y / 2);

        dl.addImage(getIcon(log.getType()), pos.x + 10, pos.y + 10, pos.x + 40, pos.y + 40);
        dl.addText(pos.x + 45, halfHeight, ImGui.getColorU32(1, 1, 1, 1), log.getMessage());

        ImGui.setCursorScreenPos(pos.x, pos.y + 50.0f);
        if (scroll) {
            if (ImGui.getScrollMaxY() > 0) {
                ImGui.setScrollY(ImGui.getScrollMaxY());
            }

            scroll = false;
        }
    }

    private static int getIcon(Log.LogType type) {
        switch (type) {
            case INFO -> {
                return Icons.getIcon("info");
            }
            case WARNING -> {
                return Icons.getIcon("warning");
            }
            case ERROR -> {
                return Icons.getIcon("error");
            }
            default -> {
                return -1;
            }
        }
    }

    public static void log(Object message, Log.LogType type) {
        logs.add(new Log(message == null ? "null" : message.toString(), type));
        checkLogSize();
        scroll = true;
    }

    public static void info(Object message) {
        log(message, Log.LogType.INFO);
    }

    public static void warning(Object message) {
        log(message, Log.LogType.WARNING);
    }

    public static void error(Object message) {
        log(message, Log.LogType.ERROR);
    }

    public static void error(Exception e) {
        error(e.getMessage());
    }

    public static void checkLogSize() {
        if (logs.size() > MAX_LOGS) {
            logs.remove(0);
        }
    }

    public static void clear() {
        logs.clear();
    }

    public static List<Log> getLogs() {
        return logs;
    }

}
