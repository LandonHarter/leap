package org.landon.util;

import org.landon.editor.windows.logger.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class FileUtil {

    public static String readFile(File f) {
        try {
            final BufferedReader in = new BufferedReader(
                    new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = in.readLine()) != null) {
                result.append(line).append("\n");
            }
            in.close();

            return result.toString();
        } catch (IOException e) {
            System.err.println("File not found");
            return null;
        }
    }

    public static void writeFile(File f, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static boolean isExtension(File f, String[] extensions) {
        for (String extension : extensions) {
            if (f.getName().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

}
