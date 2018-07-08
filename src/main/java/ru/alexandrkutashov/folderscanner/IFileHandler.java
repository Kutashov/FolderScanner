package ru.alexandrkutashov.folderscanner;

import java.util.Date;

/**
 * Interface for entry saver component.
 */
@FunctionalInterface
public interface IFileHandler {

    void handleContent(String content, Date date);
}
