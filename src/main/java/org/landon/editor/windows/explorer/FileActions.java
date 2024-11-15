package org.landon.editor.windows.explorer;

import org.landon.editor.windows.logger.Logger;
import org.landon.scene.Scene;
import org.landon.scene.SceneManager;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.function.Consumer;

public class FileActions {

    private static HashMap<String, Consumer<File>> actions = new HashMap<>();

    public static void init() {
        actions.put("leap", (file) -> {
            JFrame frame = new JFrame();
            if (JOptionPane.showConfirmDialog(frame, "Do you want to open this scene?") == JOptionPane.YES_OPTION) {
                Scene scene = SceneManager.readScene(file);
                SceneManager.loadScene(scene);
            }
            frame.dispose();
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
