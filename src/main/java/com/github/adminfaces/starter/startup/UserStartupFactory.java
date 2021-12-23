package com.github.adminfaces.starter.startup;

import com.github.adminfaces.starter.infra.daohbase.UserDao;
import com.github.adminfaces.starter.model.hbase.Role;
import com.github.adminfaces.starter.model.hbase.User;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.IOException;

@Startup
@DependsOn("HbaseConnection")
@Singleton
public class UserStartupFactory {

    @Inject
    UserDao userDao;

    @PostConstruct
    private void init() {
        try {
            if (userDao.findAll().isEmpty()) {

                    User user = new User();
                    user.setNom("Admin");
                    user.setPrenom("Admin");
                    user.setRole(Role.admin);
                    user.setEmail("admin@gmail.com");
                    user.setUsername("admin");
                    user.setPassword("1234");
                    userDao.save(user);
                    System.out.println("Admin User created");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}