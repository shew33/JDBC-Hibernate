package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
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
                throw new RuntimeException("не найден файл .properties");
            }
            properties.load(input);
        } catch (IOException e) {
            logger.error("ошибка при загрузке .properties", e);
            throw new RuntimeException("ошибка при загрузке .properties", e);
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
            throw new RuntimeException("ошибка подключения к БД", e);
        }
    }

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            Properties settings = new Properties();

            settings.put(Environment.DRIVER, properties.getProperty("db.driver"));
            settings.put(Environment.URL, properties.getProperty("db.url"));
            settings.put(Environment.USER, properties.getProperty("db.user"));
            settings.put(Environment.PASS, properties.getProperty("db.password"));
            settings.put(Environment.DIALECT, properties.getProperty("hibernate.dialect"));
            settings.put(Environment.SHOW_SQL, properties.getProperty("hibernate.show_sql"));
            settings.put(Environment.HBM2DDL_AUTO, properties.getProperty("hibernate.hbm2ddl_auto"));

            configuration.setProperties(settings);
            configuration.addAnnotatedClass(User.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            logger.error("ошибка создания SessionFactory", e);
            throw new RuntimeException("ошибка создания SessionFactory", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

