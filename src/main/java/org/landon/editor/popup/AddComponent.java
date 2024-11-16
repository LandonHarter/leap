package org.landon.editor.popup;

import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import org.apache.log4j.BasicConfigurator;
import org.landon.components.Component;
import org.landon.editor.Icons;
import org.landon.editor.windows.inspector.Inspector;
import org.landon.editor.windows.logger.Logger;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AddComponent extends Popup {

    private HashMap<String, List<Component>> submenus;

    private ImString search = new ImString();
    private List<Component> searchResults = new ArrayList<>();

    @Override
    public void init() {
        try {
            BasicConfigurator.configure();
            Reflections reflections = new Reflections("org.landon.components");
            submenus = new HashMap<>();
            Set<Class<? extends Component>> components = reflections.getSubTypesOf(Component.class);
            for (Class<? extends Component> c : components) {
                String packageName = c.getPackage().getName().replace("org.landon.components", "");
                if (packageName.startsWith(".")) {
                    packageName = packageName.substring(1, 2).toUpperCase() + packageName.substring(2);
                }

                if (submenus.containsKey(packageName)) {
                    submenus.get(packageName).add(c.getDeclaredConstructor().newInstance());
                } else {
                    ArrayList<Component> list = new ArrayList<>();
                    list.add(c.getDeclaredConstructor().newInstance());
                    submenus.put(packageName, list);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    @Override
    public void renderBase() {
        ImGui.setNextWindowSize(350, 300);
        if (ImGui.beginPopup(getId())) {
            render();
            ImGui.endPopup();
        }
    }

    @Override
    public void render() {
        ImGui.setNextItemWidth(ImGui.getContentRegionAvailX() - ImGui.calcTextSize("Search").x - 10);
        if (ImGui.inputText("Search", search)) search();
        ImGui.setCursorPosY(ImGui.getCursorPosY() + 4);

        if (search.isEmpty()) renderSubmenus();
        else renderSearch();
    }

    private void renderSubmenus() {
        for (String submenu : submenus.keySet()) {
            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 6, 6);
            ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 4);
            boolean open = ImGui.treeNodeEx(submenu, ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.Framed);
            ImGui.popStyleVar(2);
            if (open) {
                ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 2, 6);
                ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 4);
                for (Component c : submenus.get(submenu)) {
                    renderComponent(c);
                }
                ImGui.popStyleVar(2);
                ImGui.treePop();
            }
        }
        ImGui.setCursorPosY(ImGui.getCursorPosY() + 4);
    }

    private void renderSearch() {
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 6, 6);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 4);
        searchResults.forEach(this::renderComponent);
        ImGui.popStyleVar(2);
    }

    private void renderComponent(Component c) {
        float cursorY = ImGui.getCursorPosY();
        ImGui.setCursorPosY(cursorY + 4);
        ImGui.setCursorPosX(ImGui.getCursorPosX() + 6);
        ImGui.image(Icons.getIcon(c.getName()), 21, 21);
        ImGui.sameLine();
        ImGui.setCursorPosY(cursorY);
        if (ImGui.treeNodeEx(c.getName(), ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.Framed | ImGuiTreeNodeFlags.Leaf)) {
            if (ImGui.isItemClicked()) {
                Inspector.getSelectedObject().addComponent(c.clone());
                close();
            }
            ImGui.treePop();
        }
        ImGui.setCursorPosY(ImGui.getCursorPosY() + 2);
    }

    private void search() {
        searchResults.clear();
        for (List<Component> components : submenus.values()) {
            for (Component c : components) {
                if (c.getName().toLowerCase().contains(search.get().toLowerCase())) {
                    searchResults.add(c);
                }
            }
        }
    }

}
