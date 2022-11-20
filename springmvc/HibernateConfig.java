package com.javaunit3.springmvc;


import com.javaunit3.springmvc.model.MovieEntity;
import com.javaunit3.springmvc.model.VoteEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Spring annotation on the class so that Spring recognizes beans defined in the class
@Configuration
public class HibernateConfig {

    // Define a Spring bean method that returns hibernate SessionFactory.
    // The session factory should point to the hibernate.cfg.xml
    // file we created earlier
    @Bean
    public SessionFactory getFactory() {

        // add MovieEntity and VoteEntity as an annotated class to the SessionFactory
            // tells Hibernate to use these entities/ models/ XML file details

        SessionFactory factory = new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(MovieEntity.class)
                .addAnnotatedClass(VoteEntity.class)
                .buildSessionFactory();

        return factory;
    }
}
