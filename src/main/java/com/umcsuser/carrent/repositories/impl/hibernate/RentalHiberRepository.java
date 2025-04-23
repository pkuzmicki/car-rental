package com.umcsuser.carrent.repositories.impl.hibernate;

import com.umcsuser.carrent.db.HibernateConfig;
import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.RentalRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

public class RentalHiberRepository implements RentalRepository {
    @Override
    public List<Rental> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Rental", Rental.class).list();
        }
    }

    @Override
    public Optional<Rental> findById(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Rental.class, id));
        }
    }

    @Override
    public Rental save(Rental rental) {
        if (rental.getId() == null || rental.getId().isBlank()) {
            rental.setId(UUID.randomUUID().toString());
        }

        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(rental);
            tx.commit();
            return rental;
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
            Rental rental = session.get(Rental.class, id);
            if (rental != null) {
                session.delete(rental);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query<Rental> query = session.createQuery("FROM Rental rental " +
                    "WHERE rental.vehicleId = :vehicleId " +
                    "AND rental.returnDate IS NULL", Rental.class);
            query.setParameter("vehicleId", vehicleId);
            return query.uniqueResultOptional();
        }
    }
}
