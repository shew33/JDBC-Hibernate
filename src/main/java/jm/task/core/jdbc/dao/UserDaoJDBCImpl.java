package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import jm.task.core.jdbc.dao.JdbcCommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);
    private final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {}

    @Override
    public void createUsersTable() {
        JdbcCommandExecutor.executeCommand(connection, conn -> {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(SqlQueries.CREATE_USERS_TABLE);
            }
        });
    }

    @Override
    public void dropUsersTable() {
        JdbcCommandExecutor.executeCommand(connection, conn -> {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(SqlQueries.DROP_USERS_TABLE);
            }
        });
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        JdbcCommandExecutor.executeCommand(connection, conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement(SqlQueries.SAVE_USER)) {
                pstmt.setString(1, name);
                pstmt.setString(2, lastName);
                pstmt.setByte(3, age);
                pstmt.executeUpdate();
                logger.info("User с именем – {} добавлен в базу данных", name);
            }
        });
    }

    @Override
    public void removeUserById(long id) {
        JdbcCommandExecutor.executeCommand(connection, conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement(SqlQueries.REMOVE_USER_BY_ID)) {
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        return JdbcCommandExecutor.executeQuery(connection, conn -> {
            List<User> users = new ArrayList<>();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SqlQueries.GET_ALL_USERS)) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setLastName(rs.getString("lastName"));
                    user.setAge(rs.getByte("age"));
                    users.add(user);
                }
            }
            return users;
        });
    }

    @Override
    public void cleanUsersTable() {
        JdbcCommandExecutor.executeCommand(connection, conn -> {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(SqlQueries.CLEAN_USERS_TABLE);
            }
        });
    }
}
