package org.landon.project;

import java.io.File;

public class Project {

    private static File rootDirectory;
    private static ProjectConfig config;

    public static void load(File rootDirectory) {
        Project.rootDirectory = rootDirectory;
        Project.config = ProjectConfig.load(rootDirectory);
    }

    public File getRootDirectory() {
        return rootDirectory;
    }

    public ProjectConfig getConfig() {
        return config;
    }

    public static String getName() {
        return config.getName();
    }

    public static void setName(String name) {
        config.setName(name);
        config.save(rootDirectory);
    }

}
