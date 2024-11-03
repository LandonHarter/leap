package org.landon.editor;

import org.landon.graphics.Texture;

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

        addIcon("leap", "resources/icons/logo.png");
    }

    public static int getIcon(String name) {
        return icons.get(name.toLowerCase());
    }

    private static void addIcon(String name, String filepath) {
        icons.put(name, Texture.loadTexture(filepath));
    }

}
