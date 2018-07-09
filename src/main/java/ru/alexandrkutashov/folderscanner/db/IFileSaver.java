package ru.alexandrkutashov.folderscanner.db;

import java.util.Date;

/**
 * Interface for entry saver component.
 */
@FunctionalInterface
public interface IFileSaver {

    int SAVE_ERROR_ID = -1;

    /**
     * Save content to destination source
     * @param content entity content
     * @param date entity date
     * @return id of a new record, or {@code SAVE_ERROR_ID} if some error occurred
     */
    int saveContent(String content, Date date);
}
