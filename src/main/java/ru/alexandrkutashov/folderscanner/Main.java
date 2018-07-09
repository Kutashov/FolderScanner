package ru.alexandrkutashov.folderscanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alexandrkutashov.folderscanner.db.DB;
import ru.alexandrkutashov.folderscanner.db.FileMover;
import ru.alexandrkutashov.folderscanner.tasks.HandlerTask;
import ru.alexandrkutashov.folderscanner.tasks.StartTask;
import ru.alexandrkutashov.folderscanner.tasks.WorkTask;
import ru.alexandrkutashov.folderscanner.xml.Entry;
import ru.alexandrkutashov.folderscanner.xml.XmlFileConverter;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.sql.SQLException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    //region params
    private static final String MONITOR_FOLDER_PARAM = "-m";
    private static final String HANDLED_FILES_FOLDER_PARAM = "-h";
    private static final String MONITOR_PERIOD_SEC_PARAM = "-p";
    private static final int PERIOD_DEFAULT = 0;
    private static final String FOLDER_DEFAULT = null;

    private static String monitorFolder = FOLDER_DEFAULT;
    private static int monitorPeriod = PERIOD_DEFAULT;
    private static String handledFilesFolder = FOLDER_DEFAULT;
    //endregion

    //region dependencies
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Queue<File> filesQueue = new ConcurrentLinkedQueue<>();
    private static final Queue<File> foldersQueue = new ConcurrentLinkedQueue<>();
    private static final Queue<Entry> databaseQueue = new ConcurrentLinkedQueue<>();
    private static final Set<File> symbolicLinkSet = new CopyOnWriteArraySet<>();
    private static final int cores = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService schedulerThreads = Executors.newFixedThreadPool(cores);
    //endregion

    public static void main(String[] args) {

        try {
            processParams(args);

            //launch cores-1 worker thread and one for handling db
            for (int i = 0; i < cores - 1; ++i) {
                schedulerThreads.execute(new WorkTask(filesQueue, foldersQueue, databaseQueue,
                        symbolicLinkSet, new XmlFileConverter()));
            }
            schedulerThreads.execute(new HandlerTask(databaseQueue, new DB(),
                    new FileMover(handledFilesFolder)));

            //schedule checking
            scheduler.scheduleAtFixedRate(new StartTask(filesQueue, foldersQueue, monitorFolder),
                    0, monitorPeriod, TimeUnit.SECONDS);
        } catch (SQLException | JAXBException | ClassNotFoundException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }
    }

    private static void processParams(String[] args) {
        for (int i = 0; i < args.length; ++i) {
            final String arg = args[i];

            switch (arg) {
                case MONITOR_FOLDER_PARAM:
                    monitorFolder = args[++i];
                    break;
                case HANDLED_FILES_FOLDER_PARAM:
                    handledFilesFolder = args[++i];
                    break;
                case MONITOR_PERIOD_SEC_PARAM:
                    monitorPeriod = Integer.valueOf(args[++i]);
                    break;
                default:
                    logger.error("Unknown param: " + arg);
            }
        }

        if (monitorFolder == FOLDER_DEFAULT) {
            throw new IllegalArgumentException("Monitor folder param (" + MONITOR_FOLDER_PARAM + ") is not set");
        }
        if (handledFilesFolder == FOLDER_DEFAULT) {
            throw new IllegalArgumentException("Handled files folder param (" + HANDLED_FILES_FOLDER_PARAM + ") is not set");
        }
        if (monitorPeriod == PERIOD_DEFAULT) {
            throw new IllegalArgumentException("Monitor folder param (" + MONITOR_PERIOD_SEC_PARAM + ") is not set");
        }
    }
}
