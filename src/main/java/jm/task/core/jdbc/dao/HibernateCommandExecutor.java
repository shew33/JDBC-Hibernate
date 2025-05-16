package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class HibernateCommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(HibernateCommandExecutor.class);

    public static void executeInsideTransaction(Consumer<Session> action) {
        Transaction transaction = null;
        Session session = null;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            action.accept(session);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    logger.error("ошибка при откате транзакции: {}", rollbackEx.getMessage(), rollbackEx);
                }
            }
            logger.error("ошибка при выполнении транзакции: {}", e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public static <R> R executeWithResult(Function<Session, R> function) {
        try (Session session = Util.getSessionFactory().openSession()) {
            return function.apply(session);
        } catch (Exception e) {
            logger.error("ошибка при выполнении запроса: {}", e.getMessage(), e);
            return null;
        }
    }
}
