package jm.task.core.jdbc;

import java.util.List;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

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
