package com.umcsuser.carrent.repositories.impl.hibernate;

import com.umcsuser.carrent.db.HibernateConfig;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.VehicleRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class VehicleHiberRepository implements VehicleRepository {
    @Override
    public void addVehicle(String id, String category, String brand, String model, int year, String plate, BigDecimal price, Map<String, Object> atributes) {

    }

    @Override
    public void removeVehicle(String id) {

    }

    @Override
    public List<Vehicle> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Vehicle", Vehicle.class).list();
        }
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Vehicle.class, id));
        }
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(vehicle);
            tx.commit();
            return vehicle;
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
            Vehicle vehicle = session.get(Vehicle.class, id);
            if (vehicle != null) {
                session.delete(vehicle);
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
