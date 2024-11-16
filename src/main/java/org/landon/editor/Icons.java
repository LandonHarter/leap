package org.landon.editor;

import org.landon.graphics.material.Texture;

import java.util.HashMap;

public final class Icons {

    private static HashMap<String, Integer> icons = new HashMap<>();

    public static void init() {
        // Components
        addIcon("gameobject", "resources/icons/misc/gameobject.png");
        addIcon("transform", "resources/icons/components/transform.png");

        addIcon("camera", "resources/icons/components/camera.png");
        addIcon("meshfilter", "resources/icons/components/meshfilter.png");
        addIcon("meshrenderer", "resources/icons/components/meshrenderer.png");
        addIcon("light", "resources/icons/components/light.png");

        addIcon("folder", "resources/icons/explorer/folder.png");
        addIcon("file", "resources/icons/explorer/file.png");
        addIcon("model", "resources/icons/explorer/model.png");

        addIcon("play", "resources/icons/misc/play.png");
        addIcon("nowplaying", "resources/icons/misc/nowplaying.png");
        addIcon("stop", "resources/icons/misc/stop.png");

        addIcon("info", "resources/icons/logger/log.png");
        addIcon("warning", "resources/icons/logger/warning.png");
        addIcon("error", "resources/icons/logger/error.png");

        addIcon("leap", "resources/icons/logo.png");
        addIcon("grid", "resources/icons/misc/grid.png");
    }

    public static int getIcon(String name) {
        return icons.getOrDefault(name.toLowerCase().replaceAll(" ", ""), 0);
    }

    private static void addIcon(String name, String filepath) {
        icons.put(name, Texture.loadTexture(filepath));
    }

}
