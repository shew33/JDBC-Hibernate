package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserServiceImpl() {
        this.userDao = loadUserDaoFromProperties();
    }

    private UserDao loadUserDaoFromProperties() {
        Properties properties = new Properties();
        try (InputStream input = UserServiceImpl.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("Файл application.properties не найден");
                throw new IllegalStateException("Файл application.properties не найден");
            }
            properties.load(input);
            String realization = properties.getProperty("realization");

            if ("jdbc".equalsIgnoreCase(realization)) {
                return new UserDaoJDBCImpl();
            } else if ("hibernate".equalsIgnoreCase(realization)) {
                return new UserDaoHibernateImpl();
            } else {
                throw new IllegalArgumentException("Некорректное значение realization: " + realization);
            }

        } catch (Exception e) {
            logger.error("Ошибка при инициализации UserDao", e);
            throw new IllegalStateException("Ошибка при инициализации UserDao", e);
        }
    }

    @Override
    public void createUsersTable() {
        userDao.createUsersTable();
    }

    @Override
    public void dropUsersTable() {
        userDao.dropUsersTable();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        userDao.saveUser(name, lastName, age);
    }

    @Override
    public void removeUserById(long id) {
        userDao.removeUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void cleanUsersTable() {
        userDao.cleanUsersTable();
    }
}
