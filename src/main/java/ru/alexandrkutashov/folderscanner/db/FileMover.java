package ru.alexandrkutashov.folderscanner.db;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileMover implements IFileMover {

    private final String movePath;

    public FileMover(String movePath) {
        this.movePath = movePath;
    }

    @Override
    public File move(File file, String fileName) throws IOException {
        if (fileName == null) {
            return Files.move(file.toPath(), new File(movePath, file.getName()).toPath()).toFile();
        } else {
            return Files.move(file.toPath(), new File(fileName).toPath()).toFile();
        }
    }
}
