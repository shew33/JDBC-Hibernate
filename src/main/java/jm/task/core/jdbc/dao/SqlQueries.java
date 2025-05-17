package jm.task.core.jdbc.dao;

public class SqlQueries {
    public static final String CREATE_USERS_TABLE = """
        CREATE TABLE IF NOT EXISTS users (
            id SERIAL PRIMARY KEY,
            name VARCHAR(50),
            lastName VARCHAR(50),
            age SMALLINT
        )
        """;

    public static final String DROP_USERS_TABLE = "DROP TABLE IF EXISTS users";
    public static final String TRUNCATE_USERS_TABLE = "TRUNCATE TABLE users";
    public static final String HQL_GET_ALL_USERS = "FROM User";
    public static final String SAVE_USER = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
    public static final String REMOVE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    public static final String GET_ALL_USERS = "SELECT * FROM users";
    public static final String CLEAN_USERS_TABLE = "TRUNCATE TABLE users";
}
