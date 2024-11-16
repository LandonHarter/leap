package org.landon.util;

import org.lwjgl.util.tinyfd.TinyFileDialogs;

public class DialogUtil {

    public static boolean confirm(String message) {
        return TinyFileDialogs.tinyfd_messageBox("Confirm", message, "okcancel", "ok", true);
    }

    public static boolean yesNo(String message) {
        return TinyFileDialogs.tinyfd_messageBox("Confirm", message, "yesno", "yesno", true);
    }

    public static void message(String message) {
        TinyFileDialogs.tinyfd_messageBox("Message", message, "ok", "info", true);
    }

    public static void warning(String message) {
        TinyFileDialogs.tinyfd_messageBox("Warning", message, "ok", "warning", true);
    }

    public static void error(String message) {
        TinyFileDialogs.tinyfd_messageBox("Error", message, "ok", "error", true);
    }

}
