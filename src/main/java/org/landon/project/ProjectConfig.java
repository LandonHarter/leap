package org.landon.project;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.landon.util.FileUtil;

import java.io.File;

public class ProjectConfig {

    private String name;
    private String lastScene;

    public ProjectConfig() {
        this.name = "New Project";
        lastScene = null;
    }

    public ProjectConfig(String name) {
        this.name = name;
        lastScene = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastScene() {
        return lastScene;
    }

    public void setLastScene(String lastScene) {
        this.lastScene = lastScene;
    }

    public void save(File projectRoot) {
        String json = JSON.toJSONString(this, JSONWriter.Feature.FieldBased, JSONWriter.Feature.WriteNulls);
        File file = new File(projectRoot, "project.json");
        FileUtil.writeFile(file, json);
    }

    public static ProjectConfig load(File projectRoot) {
        verifyProjectRoot(projectRoot);

        File file = new File(projectRoot, "project.json");
        if (!file.exists()) {
            ProjectConfig config = new ProjectConfig(projectRoot.getName());
            config.save(projectRoot);
            return config;
        }
        String json = FileUtil.readFile(file);
        return JSON.parseObject(json, ProjectConfig.class);
    }

    private static void verifyProjectRoot(File projectRoot) {
        File assets = new File(projectRoot, "assets");
        if (!assets.exists()) {
            assets.mkdir();
        }

        File scenes = new File(assets, "scenes");
        if (!scenes.exists()) {
            scenes.mkdir();
        }
    }

}
