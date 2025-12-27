package org.landon.editor.windows.inspector.fields;

import java.io.File;

public class LeapFile {

    private File file;

    public LeapFile(String filepath) {
        this.file = new File(filepath);
    }

    public LeapFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
