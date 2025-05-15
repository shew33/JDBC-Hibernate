package jm.task.core.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcCommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCommandExecutor.class);

    public static void executeCommand(Connection connection, SqlConsumer<Connection> command) {
        try {
            command.accept(connection);
        } catch (SQLException e) {
            logger.error("SQL Ошибка: {}", e.getMessage());
        }
    }

    public static <R> R executeQuery(Connection connection, SqlFunction<Connection, R> query) {
        try {
            return query.apply(connection);
        } catch (SQLException e) {
            logger.error("SQL ошибка: {}", e.getMessage());
            return null;
        }
    }
}

