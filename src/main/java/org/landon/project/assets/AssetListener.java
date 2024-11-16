package org.landon.project.assets;

import java.io.File;

public class AssetListener {

    public AssetListener() {
        Assets.addListener(this);
    }

    public void onFileCreated(File file) {}
    public void onFileDeleted(File file) {}
    public void onFileModified(File file) {}

}
