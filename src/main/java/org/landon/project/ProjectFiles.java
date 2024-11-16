package org.landon.project;

import org.landon.core.Window;
import org.landon.editor.windows.logger.Logger;
import org.landon.graphics.material.Texture;
import org.landon.project.assets.Assets;
import org.landon.util.ThreadUtil;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectFiles {

    private static final HashMap<String, List<File>> files = new HashMap<>();
    private static WatchService watchService;
    private static Thread watchThread;

    public static final String[] IMAGE_EXTENSIONS = {"png", "jpg", "jpeg"};

    public static void init() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(Project.getAssetsDirectory().getPath());
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            watchThread = ThreadUtil.run(() -> {
                while (Window.getInstance().isOpen()) {
                    watchForEvent();
                }
            });
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static void watchForEvent() {
        try {
            WatchKey key;
            while ((key = watchService.take()) != null) {
                List<WatchEvent<?>> events = key.pollEvents();
                for (WatchEvent<?> event : events) {
                    WatchEvent.Kind<?> kind = event.kind();
                    File file = new File(Project.getAssetsDirectory().getPath() + "/" + event.context());
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) Assets.triggerFileCreated(file);
                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) Assets.triggerFileDeleted(file);
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) Assets.triggerFileModified(file);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static void destroy() {
        try {
            watchThread.interrupt();
            watchService.close();
        } catch (Exception e) {
            Logger.error(e);
        }
    }

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
                    files.put(extension, new ArrayList<>(List.of(file)));
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
