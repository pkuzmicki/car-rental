package com.umcsuser.carrent.repositories.impl.hibernate;

import com.umcsuser.carrent.db.HibernateConfig;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserHiberRepository implements UserRepository {
    @Override
    public List<User> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public Optional<User> findById(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE login = :login", User.class);
            query.setParameter("login", login);
            return query.uniqueResultOptional();
        }
    }

//    @Override
//    public User save(User user) {
//        Transaction tx = null;
//        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
//            tx = session.beginTransaction();
//            session.saveOrUpdate(user);
//            tx.commit();
//            return user;
//        } catch (Exception e) {
//            if (tx != null && tx.isActive()) {
//                tx.rollback();
//            }
//            throw e;
//        }
//    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        }

        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void deleteById(String id) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
