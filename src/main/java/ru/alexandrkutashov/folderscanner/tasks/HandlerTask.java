package ru.alexandrkutashov.folderscanner.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alexandrkutashov.folderscanner.db.IFileMover;
import ru.alexandrkutashov.folderscanner.db.IFileSaver;
import ru.alexandrkutashov.folderscanner.xml.Entry;

import java.util.Queue;

/**
 * Job for saving entries to db.
 * Keep it on a single thread to exclude data version races.
 */
public class HandlerTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HandlerTask.class);

    private final Queue<Entry> handlerQueue;
    private final IFileSaver fileSaver;
    private final IFileMover fileMover;

    public HandlerTask(Queue<Entry> queue, IFileSaver handler, IFileMover mover) {
        handlerQueue = queue;
        fileSaver = handler;
        fileMover = mover;
    }

    @Override
    public void run() {
        while (true) {
            Entry entry = handlerQueue.poll();
            if (entry != null) {
                try {
                    logger.debug("Handled: " + entry.toString());
                    String oldPath = entry.getFile().getAbsolutePath();
                    entry.setFile(fileMover.move(entry.getFile(), null));
                    int result = fileSaver.saveContent(entry.getContent(), entry.getDate());
                    if (result == IFileSaver.SAVE_ERROR_ID) {
                        logger.error("Can't write content to db!");
                        //record was not applied, we should revert moving
                        entry.setFile(fileMover.move(entry.getFile(), oldPath));
                    }
                } catch (Exception e) {
                    logger.error(e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
    }
}
