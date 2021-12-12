package com.github.adminfaces.starter.startup;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.mongodb.morphia.Datastore;

import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.model.User;

@Startup
@DependsOn("MongoConnexion")
@Singleton
public class UserStartupFactory {

    @Inject
    Datastore datastore;

    @PostConstruct
    private void init() {
        if (datastore.find(User.class).countAll() == 0) {
           
                User user = new User();
                user.setNom("Admin");
                user.setPrenom("Admin");
                user.setRole(Role.admin);
                user.setEmail("admin@gmail.com");
                user.setUsername("admin");
                user.setPassword("1234");
                datastore.save(user);
            
        }
    }

}