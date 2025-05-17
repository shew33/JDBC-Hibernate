package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Util.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("файл .properties не найден");
            }
            properties.load(input);
        } catch (IOException e) {
            logger.error("ошибка при загрузке .properties", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );
        } catch (SQLException e) {
            logger.error("ошибка подключения к БД", e);
            throw new RuntimeException(e);
        }
    }

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            Properties settings = new Properties();

            settings.put("hibernate.connection.driver_class", properties.getProperty("db.driver"));
            settings.put("hibernate.connection.url", properties.getProperty("db.url"));
            settings.put("hibernate.connection.username", properties.getProperty("db.user"));
            settings.put("hibernate.connection.password", properties.getProperty("db.password"));
            settings.put("hibernate.dialect", properties.getProperty("hibernate.dialect"));
            settings.put("hibernate.show_sql", properties.getProperty("hibernate.show_sql"));
            settings.put("hibernate.hbm2ddl.auto", properties.getProperty("hibernate.hbm2ddl_auto"));

            configuration.setProperties(settings);
            configuration.addAnnotatedClass(User.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            logger.error("ошибка создания SessionFactory", e);
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

