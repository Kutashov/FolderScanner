package ru.alexandrkutashov.folderscanner.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alexandrkutashov.folderscanner.db.IFileHandler;
import ru.alexandrkutashov.folderscanner.xml.Entry;

import java.util.Queue;

/**
 * Job for saving entries to db.
 * Keep it on a single thread to exclude data version races.
 */
public class HandlerTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HandlerTask.class);

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
                try {
                    logger.debug("Handled:" + file.toString());
                    fileHandler.handleContent(file.getContent(), file.getDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
