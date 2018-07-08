package ru.alexandrkutashov.folderscanner.tasks;

import java.io.File;
import java.util.Queue;

/**
 * Periodically starter job.
 * Clears queues and fills up with a target folder.
 */
public class StartTask implements Runnable {

    private final Queue<File> filesQueue;
    private final Queue<File> foldersQueue;
    private final String scanningFolder;

    public StartTask(Queue<File> filesQueue, Queue<File> foldersQueue, String folder) {
        this.filesQueue = filesQueue;
        this.foldersQueue = foldersQueue;
        scanningFolder = folder;
    }

    @Override
    public void run() {
        foldersQueue.clear();
        filesQueue.clear();

        foldersQueue.add(new File(scanningFolder));
    }
}
