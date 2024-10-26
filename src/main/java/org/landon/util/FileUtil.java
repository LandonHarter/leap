package org.landon.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class FileUtil {

    public static String readFile(File f) {
        StringBuilder result = new StringBuilder();
        try {
            Scanner myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                result.append(myReader.nextLine() + '\n');
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            System.err.println("File is null");
        }

        return result.toString();
    }

}
