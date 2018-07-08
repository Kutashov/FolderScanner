package ru.alexandrkutashov.folderscanner.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DTO for target entries
 */
@XmlRootElement(name="Entry")
public class Entry {

    private String content;
    private Date date;

    // no-arg default constructor for JAXB
    public Entry(){}

    Entry(String content, Date date) {
        this.content = content;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    @XmlElement
    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @XmlElement(name = "creationDate")
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entry entry = (Entry) o;

        if (content != null ? !content.equals(entry.content) : entry.content != null) return false;
        return date != null ? date.equals(entry.date) : entry.date == null;
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "content='" + content + '\'' +
                ", date=" + date +
                '}';
    }

    public static class DateTimeAdapter extends XmlAdapter<String, Date> {

        private static final String DATE_FORMAT = "yyyy-dd-MM HH:mm:ss";

        private final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        @Override
        public Date unmarshal(String xml) throws Exception {
            return dateFormat.parse(xml);
        }

        @Override
        public String marshal(Date object) {
            return dateFormat.format(object);
        }
    }

}
