package com.company.filehandlingapp.environment;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class PersistenceSetup {

    private final SessionFactory sessionFactory;

    public PersistenceSetup(String configurationResourceName) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(configurationResourceName)
                .build();
        Metadata metadata = new MetadataSources(registry).buildMetadata();
        this.sessionFactory = metadata.buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session createSession() {
        return sessionFactory.openSession();
    }
}
