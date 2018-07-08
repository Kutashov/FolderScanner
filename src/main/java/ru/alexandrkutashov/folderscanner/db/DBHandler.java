package ru.alexandrkutashov.folderscanner.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Date;
import java.util.Properties;

public class DBHandler implements IFileHandler {

    private static final Logger logger = LoggerFactory.getLogger(DBHandler.class);

    private static final String DB_FILE_PROPERTY = "database.properties";
    private static final String DB_URL_PROPERTY = "db.url";
    private static final String DB_USER_PROPERTY = "db.user";
    private static final String DB_PASSWORD_PROPERTY = "db.password";
    private static final String ADD_ENTRY_STATEMENT = "INSERT INTO entries(content, created) VALUES(?, ?)";

    private final Connection connection;
    private final PreparedStatement addEntryStatement;

    public DBHandler() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Properties props = readProperties();
        String url = props.getProperty(DB_URL_PROPERTY);
        String user = props.getProperty(DB_USER_PROPERTY);
        String password = props.getProperty(DB_PASSWORD_PROPERTY);
        connection = DriverManager.getConnection(url, user, password);
        addEntryStatement = connection.prepareStatement(ADD_ENTRY_STATEMENT);
    }

    @Override
    public void handleContent(String content, Date date) throws Exception {
        addEntryStatement.setString(1, content);
        addEntryStatement.setTimestamp(2, new Timestamp(date.getTime()));
        try {
            addEntryStatement.execute();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    private static Properties readProperties() {

        Properties props = new Properties();
        Path myPath = Paths.get(DB_FILE_PROPERTY);

        try {
            BufferedReader bf = Files.newBufferedReader(myPath, StandardCharsets.UTF_8);
            props.load(bf);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }

        return props;
    }
}
