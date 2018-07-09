package ru.alexandrkutashov.folderscanner.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alexandrkutashov.folderscanner.xml.Entry;
import ru.alexandrkutashov.folderscanner.xml.IFileConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import static ru.alexandrkutashov.folderscanner.Utils.getFileExtension;

/**
 * Clue task. Polls queues for a new targets to process.
 * If the target is a folder, traverses it, looking for other folder and xml files.
 * In the case of a regular xml, checks it and adds to a processing queue.
 */
public class WorkTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WorkTask.class);

    private static final String TARGET_EXTENSION = "xml";

    private final Queue<File> filesQueue;
    private final Queue<File> foldersQueue;
    private final Queue<Entry> handleQueue;
    private final Set<File> symbolicLinkSet;
    private final IFileConverter fileConverter;

    private List<File> filesList = new LinkedList<>();
    private List<File> folderList = new LinkedList<>();

    public WorkTask(Queue<File> filesQueue, Queue<File> foldersQueue,
                    Queue<Entry> handleQueue, Set<File> symbolicLinkSet,
                    IFileConverter fileConverter) {
        this.filesQueue = filesQueue;
        this.foldersQueue = foldersQueue;
        this.handleQueue = handleQueue;
        this.symbolicLinkSet = symbolicLinkSet;
        this.fileConverter = fileConverter;
    }

    @Override
    public void run() {

        File currentFile = null;
        while (true) {

            if (currentFile == null) {
                currentFile = filesQueue.poll();
            }

            if (currentFile != null && !currentFile.isDirectory()) {

                try {
                    currentFile = checkForSymbolicLink(currentFile);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    currentFile = null;
                    continue;
                }

                execute(currentFile);
                currentFile = null;
            } else {
                File folder = currentFile;
                if (folder == null) {
                    folder = foldersQueue.poll();
                } else {
                    currentFile = null;
                }

                if (folder == null) {
                    continue;
                }

                try {
                    folder = checkForSymbolicLink(folder);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    continue;
                }

                File[] files = folder.listFiles(file -> file.isDirectory()
                        || TARGET_EXTENSION.equals(getFileExtension(file))
                        || Files.isSymbolicLink(file.toPath()));
                if (files != null) {

                    //sort files
                    for (final File fileEntry : files) {
                        if (fileEntry.isDirectory()) {
                            ((LinkedList<File>) folderList).addLast(fileEntry);
                        } else {
                            ((LinkedList<File>) filesList).addLast(fileEntry);
                        }
                    }

                    //we might have a new target to process without polling queues
                    currentFile = filesList.size() > 0 ? ((LinkedList<File>) filesList).removeFirst() :
                            (folderList.size() > 0 ? ((LinkedList<File>) folderList).removeFirst() : null);

                    if (folderList.size() > 0) {
                        foldersQueue.addAll(folderList);
                        folderList.clear();
                    }

                    if (filesList.size() > 0) {
                        filesQueue.addAll(filesList);
                        filesList.clear();
                    }
                }

            }
        }

    }

    private File checkForSymbolicLink(File file) throws IOException {
        if (Files.isSymbolicLink(file.toPath())) {
            if (symbolicLinkSet.contains(file)) {
                throw new IOException("Cyclic symlink found!: " + file);
            }
            symbolicLinkSet.add(file);
            return file.getCanonicalFile();
        } else {
            return file;
        }
    }

    private void execute(File file) {
        Entry entry = fileConverter.convert(file);
        if (entry != null) {
            //logger.debug(file.toString());
            handleQueue.add(entry);
        }
    }
}
