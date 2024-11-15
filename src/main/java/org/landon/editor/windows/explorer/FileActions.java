package org.landon.editor.windows.explorer;

import org.landon.scene.Scene;
import org.landon.scene.SceneManager;
import org.landon.util.DialogUtil;

import java.io.File;
import java.util.HashMap;
import java.util.function.Consumer;

public class FileActions {

    private static HashMap<String, Consumer<File>> actions = new HashMap<>();

    public static void init() {
        actions.put("leap", (file) -> {
            if (DialogUtil.yesNo("Do you want to open this scene?")) {
                Scene scene = SceneManager.readScene(file);
                SceneManager.loadScene(scene);
            }
        });
    }

    private static void addAction(String name, Consumer<File> action) {
        actions.put(name, action);
    }

    public static void executeAction(File file) {
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        if (actions.containsKey(extension))
            actions.get(extension).accept(file);
    }

}
