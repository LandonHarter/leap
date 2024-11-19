package org.landon;

import org.landon.core.Window;
import org.landon.editor.Editor;
import org.landon.editor.MousePicking;
import org.landon.graphics.renderers.OutlineRenderer;
import org.landon.project.Project;
import org.landon.project.ProjectFiles;
import org.landon.scene.Scene;
import org.landon.scene.SceneManager;
import org.landon.thirdparty.Discord;
import org.landon.util.ExplorerUtil;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        String projectRoot = args.length > 0 ? args[0] : ExplorerUtil.chooseDirectory();
        if (projectRoot == null) {
            System.exit(0);
        }
        Project.load(new File(projectRoot));

        Window window = new Window(1920, 1080, Project.getName() + " | Leap Game Engine");
        window.create();
        window.maximize();

        ProjectFiles.loadTextures();

        Discord.enableRpc();
        Scene scene = SceneManager.readScene(Project.getLastScene());
        SceneManager.loadScene(scene);

        scene.getSkybox().createCubemap();
        scene.editorStart();

        MousePicking.click();
        OutlineRenderer.init();

        window.closeStartupWindow();
        window.decorate();
        while (window.isOpen()) {
            window.startFrame();

            SceneManager.getCurrentScene().update(); // Renders scene to framebuffer and updates all components
            window.postRender(); // Unbinds framebuffer

            Editor.render();

            window.endFrame();
        }

        Discord.disableRpc();
        Editor.getSettings().save();
        window.destroy();
    }

}