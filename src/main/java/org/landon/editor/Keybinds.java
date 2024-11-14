package org.landon.editor;

import org.landon.components.rendering.Camera;
import org.landon.editor.windows.logger.Logger;
import org.landon.input.Input;
import org.landon.scene.GameObject;
import org.landon.scene.Scene;
import org.landon.scene.SceneManager;
import org.landon.util.ExplorerUtil;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Keybinds {

    private static final HashMap<int[], Runnable> keybinds = new HashMap<>();
    private static final List<Boolean> keybindDown = new ArrayList<>();

    public static void init() {
        registerKeybind(new int[] {
                GLFW.GLFW_KEY_LEFT_CONTROL,
                GLFW.GLFW_KEY_S
        }, () -> {
            Scene currentScene = SceneManager.getCurrentScene();
            if (currentScene.getFile() != null) {
                currentScene.save();
            } else {
                String path = ExplorerUtil.createFile(new String[] { "Leap Scene" }, new String[] { "leap" });
                if (path != null) {
                    currentScene.setFile(new File(path));
                    currentScene.save();
                }
            }
        });
        registerKeybind(new int[] {
                GLFW.GLFW_KEY_LEFT_CONTROL,
                GLFW.GLFW_KEY_O
        }, () -> {
            String path = ExplorerUtil.chooseFile(new String[] { "leap" });
            if (path != null) {
                Scene scene = SceneManager.readScene(new File(path));
                SceneManager.loadScene(scene);
            }
        });
        registerKeybind(new int[] {
                GLFW.GLFW_KEY_LEFT_CONTROL,
                GLFW.GLFW_KEY_N
        }, () -> {
            Scene scene = new Scene("Untitled Scene",  false);
            GameObject camera = new GameObject("Camera");
            camera.addComponent(new Camera());
            scene.addObject(camera);
            SceneManager.loadScene(scene);
        });
    }

    public static void update() {
        if (Editor.isPlaying()) return;

        int index = 0;
        for (int[] keys : keybinds.keySet()) {
            boolean runAction = true;
            for (int key : keys) {
                if (!Input.isKeyDown(key)) {
                    runAction = false;
                    break;
                }
            }

            if (runAction && !keybindDown.get(index)) {
                keybinds.get(keys).run();
                keybindDown.set(index, true);
            } else if (!runAction && keybindDown.get(index)) {
                keybindDown.set(index, false);
            }
            index++;
        }
    }

    private static void registerKeybind(int[] key, Runnable action) {
        keybinds.put(key, action);
        keybindDown.add(false);
    }

}
