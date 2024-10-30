package org.landon.editor.windows;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import org.landon.scene.SceneManager;

public final class SceneHierarchy {

    public static void render() {
        ImGui.begin("Scene Hierarchy");

        if (ImGui.collapsingHeader(SceneManager.getCurrentScene().getName(), ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.DefaultOpen)) {
            for (int i = 0; i < SceneManager.getCurrentScene().getObjects().size(); i++) {
                ImGui.text(SceneManager.getCurrentScene().getObjects().get(i).getName());
                if (ImGui.isItemClicked()) {
                    Inspector.setSelectedObject(SceneManager.getCurrentScene().getObjects().get(i));
                }
            }
        }

        ImGui.end();
    }

}
