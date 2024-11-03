package org.landon.util;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.nfd.NFDOpenDialogArgs;
import org.lwjgl.util.nfd.NFDSaveDialogArgs;
import org.lwjgl.util.nfd.NativeFileDialog;

public class ExplorerUtil {

    private static boolean initialized = false;

    public static String chooseDirectory() {
        if (!initialized) {
            NativeFileDialog.NFD_Init();
            initialized = true;
        }

        PointerBuffer outPath = MemoryUtil.memAllocPointer(1);
        int result = NativeFileDialog.NFD_PickFolder(outPath, "");
        if (result != NativeFileDialog.NFD_OKAY) {
            return null;
        }

        String path = outPath.getStringUTF8(0);
        MemoryUtil.memFree(outPath);
        return path;
    }

    public static String chooseFile(String[] extensions) {
        if (!initialized) {
            NativeFileDialog.NFD_Init();
            initialized = true;
        }

        PointerBuffer outPath = MemoryUtil.memAllocPointer(1);
        NFDOpenDialogArgs args = NFDOpenDialogArgs.create();
        int result = NativeFileDialog.NFD_OpenDialog_With(outPath, args);
        if (result != NativeFileDialog.NFD_OKAY) {
            return null;
        }

        String path = outPath.getStringUTF8(0);
        MemoryUtil.memFree(outPath);
        return path;
    }

    public static String createFile(String[] extensions) {
        if (!initialized) {
            NativeFileDialog.NFD_Init();
            initialized = true;
        }

        PointerBuffer outPath = MemoryUtil.memAllocPointer(1);
        NFDSaveDialogArgs args = NFDSaveDialogArgs.create();
        int result = NativeFileDialog.NFD_SaveDialog_With(outPath, args);
        if (result != NativeFileDialog.NFD_OKAY) {
            return null;
        }

        String path = outPath.getStringUTF8(0);
        MemoryUtil.memFree(outPath);
        return path;
    }

}
