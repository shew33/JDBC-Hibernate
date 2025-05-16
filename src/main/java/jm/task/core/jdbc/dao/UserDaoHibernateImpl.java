package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.dao.HibernateCommandExecutor.*;
import static jm.task.core.jdbc.dao.SqlQueries.*;

public class UserDaoHibernateImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernateImpl.class);

    public UserDaoHibernateImpl() {}

    @Override
    public void createUsersTable() {
        executeInsideTransaction(session ->
                session.createNativeQuery(CREATE_USERS_TABLE).executeUpdate()
        );
        logger.info("таблица пользователей успешно создана");
    }

    @Override
    public void dropUsersTable() {
        executeInsideTransaction(session ->
                session.createNativeQuery(DROP_USERS_TABLE).executeUpdate()
        );
        logger.info("таблица пользователей удалена");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeInsideTransaction(session ->
                session.save(new User(name, lastName, age))
        );
        logger.info("User с именем – {} добавлен в базу данных", name);
    }

    @Override
    public void removeUserById(long id) {
        executeInsideTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                logger.info("User с ID {} удалён", id);
            } else {
                logger.warn("User с ID {} не найден", id);
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = executeWithResult(session ->
                session.createQuery(HQL_GET_ALL_USERS, User.class).getResultList()
        );
        if (users != null) {
            logger.info("получено {} пользователей", users.size());
            return users;
        } else {
            logger.warn("список пользователей пуст или не инициализирован.");
            return new ArrayList<>();
        }
    }

    @Override
    public void cleanUsersTable() {
        executeInsideTransaction(session ->
                session.createNativeQuery(TRUNCATE_USERS_TABLE).executeUpdate()
        );
        logger.info("таблица пользователей очищена.");
    }
}
