package ru.alexandrkutashov.folderscanner;

import java.io.File;

public class Utils {

    /**
     * Get file extension. Returns null for unknown extension.
     * @param file file to check
     * @return extension name
     */
    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            int last = name.lastIndexOf(".");
            if (last == -1) {
                return null;
            }
            return name.substring(last + 1);
        } catch (Exception e) {
            return null;
        }
    }
}
