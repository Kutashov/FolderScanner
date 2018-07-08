package ru.alexandrkutashov.folderscanner.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Default implementation for {@link IFileConverter}
 */
public class XmlFileConverter implements IFileConverter {

    private final Unmarshaller jaxbUnmarshaller;

    public XmlFileConverter() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Entry.class);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    }

    @Override
    public Entry convert(File file) {
        try {
            Entry actual = (Entry) jaxbUnmarshaller.unmarshal(file);
            if (actual.getContent() != null && actual.getDate() != null) {
                return actual;
            }
        } catch (JAXBException e) {
            return null;
        }
        return null;
    }
}
