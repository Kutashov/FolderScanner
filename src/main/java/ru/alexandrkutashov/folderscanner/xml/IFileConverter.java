package ru.alexandrkutashov.folderscanner.xml;

import java.io.File;

/**
 * File to Entry converter
 */
@FunctionalInterface
public interface IFileConverter {

    /**
     * Converts file to entry. Returns null if any field is null
     * @param file input file
     * @return converted entry or null
     */
    Entry convert(File file);
}
