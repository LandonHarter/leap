package org.landon.editor.popup;

import imgui.ImGui;
import imgui.flag.ImGuiHoveredFlags;
import imgui.flag.ImGuiPopupFlags;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Popup {

    protected final String id;
    protected boolean open;

    private static final List<Popup> popups = new ArrayList<>();
    private static final List<String> popupsToOpen = new ArrayList<>();

    public Popup() {
        this.id = UUID.randomUUID().toString();
        popups.add(this);

        init();
    }

    public void renderBase() {
        if (ImGui.beginPopup(id)) {
            render();
            ImGui.endPopup();
        }
    }

    public void init() {}
    public void render() {}

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
        popupsToOpen.add(id);
    }

    public void close() {
        open = false;
        ImGui.closeCurrentPopup();
    }

    public String getId() {
        return id;
    }

    public static void renderPopups() {
        popupsToOpen.forEach(ImGui::openPopup);
        popupsToOpen.clear();

        for (int i = 0; i < popups.size(); i++) {
            popups.get(i).renderBase();
        }
    }

}
