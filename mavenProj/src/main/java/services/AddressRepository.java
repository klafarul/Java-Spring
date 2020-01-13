package services;


import models.address.AddressEntity;
import models.person.PersonEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AddressRepository {

    @Autowired
    SessionFactory sessionFactory;

//    @Autowired
//    public AddressRepository(SessionFactory sessionFactory){
//        org.hibernate.cfg.Configuration configuration = new Configuration().configure();
//        configuration.addAnnotatedClass(AddressEntity.class);
//        configuration.addAnnotatedClass(PersonEntity.class);
//        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
//        sessionFactory = configuration.buildSessionFactory(builder.build());
//        this.sessionFactory = sessionFactory;
//
//
//    }


    public void save(AddressEntity addressEntity){
//        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();

        Session session = sessionFactory.openSession();

        Transaction tx = session.beginTransaction();
        session.save(addressEntity);
        tx.commit();
        session.close();
    }

    public void update(AddressEntity addressEntity, PersonEntity personEntity){
//        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        addressEntity = session.get(AddressEntity.class,addressEntity.getId());
        addressEntity.addPerson(personEntity);
        session.merge(addressEntity);
        tx.commit();
        session.close();
    }

    public AddressEntity find(AddressEntity addressEntity){
//        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Session session = sessionFactory.openSession();
        String hql = "FROM AddressEntity AE WHERE (AE.city=:city) AND (AE.house=:house) AND (AE.apartment=:apartment)";
        Query query = session.createQuery(hql);
        query.setParameter("city", (String)addressEntity.getCity());
        query.setParameter("house", addressEntity.getHouse());
        query.setParameter("apartment", addressEntity.getApartment());

        ArrayList<AddressEntity> addressEntities = (ArrayList<AddressEntity>) query.list();
        session.close();

        if (addressEntities.size() > 0){
            return addressEntities.get(0);
        }
        return null;
    }

    public ArrayList<AddressEntity> findAll(){
//        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Session session = sessionFactory.openSession();
        ArrayList<AddressEntity> addressesEntity = (ArrayList<AddressEntity>) session.createQuery("select distinct  AE FROM AddressEntity AE left JOIN fetch AE.persons order by AE.city", AddressEntity.class).list();
        System.out.println("LOOOOOK HERE: " + addressesEntity.size());
        session.close();
        return addressesEntity;
    }

    public int getId(AddressEntity address){
//        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Session session = sessionFactory.openSession();
        AddressEntity addressEntity = find(address);
        session.close();
        return addressEntity.getId();
    }
}