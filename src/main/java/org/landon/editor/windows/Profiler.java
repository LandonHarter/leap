package org.landon.editor.windows;

import imgui.ImGui;
import org.landon.core.Window;

import com.sun.management.OperatingSystemMXBean;
import org.landon.gui.Gui;

import java.lang.management.ManagementFactory;

public class Profiler {

    private static final OperatingSystemMXBean os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public static void render() {
        ImGui.begin("\uf625  Profiler");

        ImGui.text("CPU Usage: " + Math.round((float) os.getCpuLoad() * 100) + "%");

        float frameTime = Window.getInstance().getFrameTimer().getTime();
        ImGui.text("Frame Time: " + frameTime + "ms");
        ImGui.text("FPS: " + (int) (1000 / frameTime));

        ImGui.end();
    }

}
