package ru.alexandrkutashov.folderscanner;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static ru.alexandrkutashov.folderscanner.Utils.getFileExtension;

/**
 * Test for {@link Utils}
 */
public class UtilsTest {

    @Test
    public void fileExtension() {
        assertEquals("xml", getFileExtension(new File("check.xml")));
        assertEquals("xml", getFileExtension(new File("path/check.xml")));
        assertEquals(null, getFileExtension(new File("path/check")));
    }
}
