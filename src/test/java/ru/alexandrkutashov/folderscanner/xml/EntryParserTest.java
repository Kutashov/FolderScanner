package ru.alexandrkutashov.folderscanner.xml;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link XmlFileConverter}
 */
public class EntryParserTest {

    IFileConverter fileConverter = new XmlFileConverter();

    public EntryParserTest() throws JAXBException {
    }

    @Test
    public void parse() {
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        calendar.set(2014, 0, 10, 23, 41, 5);
        Entry expected = new Entry("Some content", calendar.getTime());

        File file = new File(getClass().getClassLoader()
                .getResource("entry.xml").getFile());
        Entry actual = fileConverter.convert(file);

        assertEquals(expected, actual);
    }

    @Test
    public void parseWrongFile() {
        Entry expected = null;

        File file = new File(getClass().getClassLoader()
                .getResource("wrong_entry.xml").getFile());
        Entry actual = fileConverter.convert(file);
        assertEquals(expected, actual);
    }
}
