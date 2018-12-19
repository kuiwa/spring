package com.hibernate.exceltojavabean;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
 
 
public class  HibernateUtils {
 
    public  static SessionFactory sessionFactory;
    public static Transaction transaction;
    public static  Session session;
    static {
        
        Configuration config = new Configuration().configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        sessionFactory = config.buildSessionFactory(serviceRegistry);
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }
    public static void closeSession(Session session){
        if(session!=null){
            session.close();
        }
    }
     
}
