package ru.alexandrkutashov.folderscanner.db;

import java.io.File;
import java.io.IOException;

/**
 * Helper for moving files
 */
@FunctionalInterface
public interface IFileMover {

    /**
     * Moves file to default dir
     *
     * @param file     file to move
     * @param fileName optional param to specify new file name.
     *                 If not set, default path will be destination.
     * @return new file
     */
    File move(File file, String fileName) throws IOException;
}
