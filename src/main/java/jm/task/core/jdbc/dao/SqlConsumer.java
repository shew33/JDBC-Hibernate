package jm.task.core.jdbc.dao;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlConsumer<T> {
    void accept(T t) throws SQLException;
}