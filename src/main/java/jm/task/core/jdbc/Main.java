package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Properties properties = new Properties();

        // Загружаем application.properties
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("application.properties не найден");
                return;
            }
            properties.load(input);
        } catch (Exception e) {
            logger.error("Ошибка при загрузке application.properties", e);
            return;
        }

        // Определяем реализацию DAO
        String realization = properties.getProperty("realization");
        UserDao userDao;
        if ("jdbc".equalsIgnoreCase(realization)) {
            userDao = new UserDaoJDBCImpl();
            logger.info("Используется реализация: JDBC");
        } else if ("hibernate".equalsIgnoreCase(realization)) {
            userDao = new UserDaoHibernateImpl();
            logger.info("Используется реализация: Hibernate");
        } else {
            logger.error("Недопустимое значение realization в application.properties: {}", realization);
            return;
        }

        // Инжектим реализацию в сервис
        UserService userService = new UserServiceImpl(userDao);

        userService.createUsersTable();

        userService.saveUser("Юрий", "Шевченко", (byte) 27);
        userService.saveUser("Антон", "Второй", (byte) 37);
        userService.saveUser("Сергей", "Третий", (byte) 41);
        userService.saveUser("Анатолий", "Четвертый", (byte) 22);

        List<User> users = userService.getAllUsers();
        if (users != null && !users.isEmpty()) {
            users.forEach(user -> logger.info(user.toString()));
        } else {
            logger.warn("Список пользователей пуст или не инициализирован.");
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
