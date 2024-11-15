package org.landon.project.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Assets {

    private static final List<AssetListener> listeners = new ArrayList<>();


    public static void addListener(AssetListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(AssetListener listener) {
        listeners.remove(listener);
    }

    public static void triggerFileCreated(File file) {
        for (AssetListener listener : listeners) {
            listener.onFileCreated(file);
        }
    }

    public static void triggerFileDeleted(File file) {
        for (AssetListener listener : listeners) {
            listener.onFileDeleted(file);
        }
    }

    public static void triggerFileModified(File file) {
        for (AssetListener listener : listeners) {
            listener.onFileModified(file);
        }
    }

}
