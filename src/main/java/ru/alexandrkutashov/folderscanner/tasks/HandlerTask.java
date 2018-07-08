package ru.alexandrkutashov.folderscanner.tasks;

import ru.alexandrkutashov.folderscanner.IFileHandler;
import ru.alexandrkutashov.folderscanner.xml.Entry;

import java.util.Queue;

/**
 * Job for saving entries to db.
 * Keep it on a single thread to minimize table locks.
 */
public class HandlerTask implements Runnable {

    private final Queue<Entry> handlerQueue;
    private final IFileHandler fileHandler;

    public HandlerTask(Queue<Entry> queue, IFileHandler handler) {
        handlerQueue = queue;
        fileHandler = handler;
    }

    @Override
    public void run() {
        while (true) {
            Entry file = handlerQueue.poll();
            if (file != null) {
                fileHandler.handleContent(file.getContent(), file.getDate());
            }
        }
    }
}
