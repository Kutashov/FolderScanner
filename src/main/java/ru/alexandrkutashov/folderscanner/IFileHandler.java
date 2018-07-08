package ru.alexandrkutashov.folderscanner;

import java.sql.Date;

public interface IFileHandler {

    void handleContent(String content, Date date);
}
