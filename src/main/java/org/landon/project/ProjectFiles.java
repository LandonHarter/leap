package org.landon.project;

import org.landon.graphics.Texture;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectFiles {

    private static HashMap<String, List<File>> files = new HashMap<>();

    public static final String[] IMAGE_EXTENSIONS = {"png", "jpg", "jpeg"};

    public static void parseFiles(File rootDirectory) {
        if (rootDirectory == null) return;

        for (File file : rootDirectory.listFiles()) {
            if (file.isDirectory()) {
                parseFiles(file);
            } else {
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                if (files.containsKey(extension)) {
                    files.get(extension).add(file);
                } else {
                    files.put(extension, List.of(file));
                }
            }
        }
    }

    public static void loadTextures() {
        for (File file : getFiles(IMAGE_EXTENSIONS)) {
            Texture.loadTexture(file.getPath());
        }
    }

    public static List<File> getFiles(String extension) {
        return files.getOrDefault(extension, List.of());
    }

    public static List<File> getFiles(String[] extensions) {
        List<File> allFiles = new ArrayList<>();
        for (String extension : extensions) {
            allFiles.addAll(getFiles(extension));
        }
        return allFiles;
    }

}
