package ru.alexandrkutashov.folderscanner.tasks;

import ru.alexandrkutashov.folderscanner.IFileHandler;

import java.io.File;
import java.util.Queue;

public class HandlerTask implements Runnable {

    private final Queue<File> handlerQueue;
    private final IFileHandler fileHandler;

    public HandlerTask(Queue<File> queue, IFileHandler handler) {
        handlerQueue = queue;
        fileHandler = handler;
    }

    @Override
    public void run() {
        while (true) {
            File file = handlerQueue.poll();
            if (file != null) {
                fileHandler.handleContent(null, null);
            }
        }
    }
}
