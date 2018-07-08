package ru.alexandrkutashov.folderscanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alexandrkutashov.folderscanner.tasks.HandlerTask;
import ru.alexandrkutashov.folderscanner.tasks.StartTask;
import ru.alexandrkutashov.folderscanner.tasks.WorkTask;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    //region dependencies
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Queue<File> filesQueue = new ConcurrentLinkedQueue<>();
    private static final Queue<File> foldersQueue = new ConcurrentLinkedQueue<>();
    private static final Queue<File> databaseQueue = new ConcurrentLinkedQueue<>();
    private static final int cores = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService schedulerThreads = Executors.newFixedThreadPool(cores);
    //endregion

    public static void main(String[] args) {
        for (int i = 0; i < cores - 1; ++i) {
            schedulerThreads.execute(new WorkTask(filesQueue, foldersQueue, databaseQueue));
        }
        schedulerThreads.execute(new HandlerTask(databaseQueue,
                (content, date) -> logger.debug("Found: " + content + " " + date)));
        scheduler.scheduleAtFixedRate(new StartTask(filesQueue, foldersQueue, "C:\\Programming\\android-sdk"),
                0, 8, TimeUnit.SECONDS);
    }
}
