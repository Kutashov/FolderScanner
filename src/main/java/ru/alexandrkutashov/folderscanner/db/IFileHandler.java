package ru.alexandrkutashov.folderscanner.db;

import java.util.Date;

/**
 * Interface for entry saver component.
 */
@FunctionalInterface
public interface IFileHandler {

    void handleContent(String content, Date date) throws Exception;
}
