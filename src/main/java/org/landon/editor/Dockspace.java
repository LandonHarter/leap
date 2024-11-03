package org.landon.editor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.landon.components.Camera;
import org.landon.core.Window;
import org.landon.scene.GameObject;
import org.landon.scene.Scene;
import org.landon.scene.SceneManager;
import org.landon.util.ExplorerUtil;

import java.io.File;

public final class Dockspace {

    public static void render() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
        ImGui.setNextWindowPos(0, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getInstance().getWidth(), Window.getInstance().getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin(" ", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        ImGui.pushStyleColor(ImGuiCol.DockingEmptyBg, 0);
        ImGui.dockSpace(ImGui.getID("dockspace"));
        ImGui.popStyleColor();

        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("New Scene")) {
                Scene scene = new Scene("Untitled Scene",  false);
                GameObject camera = new GameObject("Camera");
                camera.addComponent(new Camera());
                scene.addObject(camera);
                SceneManager.loadScene(scene);
            }
            if (ImGui.menuItem("Open Scene")) {
                String path = ExplorerUtil.chooseFile(new String[] { "leap" });
                if (path != null) {
                    Scene scene = SceneManager.readScene(new File(path));
                    SceneManager.loadScene(scene);
                }
            }
            if (ImGui.menuItem("Save Scene")) {
                Scene currentScene = SceneManager.getCurrentScene();
                if (currentScene.getFile() != null) {
                    currentScene.save();
                } else {
                    String path = ExplorerUtil.createFile(new String[] { "leap" });
                    if (path != null) {
                        currentScene.setFile(new File(path));
                        currentScene.save();
                    }
                }
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }

}
