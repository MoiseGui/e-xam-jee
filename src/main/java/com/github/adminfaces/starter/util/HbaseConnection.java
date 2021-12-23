package com.github.adminfaces.starter.util;

import com.github.adminfaces.starter.infra.daohbase.ExamenDao;
import com.github.adminfaces.starter.infra.daohbase.UserDao;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.IOException;

@Startup
@Singleton
public class HbaseConnection {
    private static Connection connection;

    private static UserDao userDao;
    private static ExamenDao examenDao;


    @PostConstruct
    public static void init() {
        try {
            System.out.println("HbaseConnection.init()");
            connection = ConnectionFactory.createConnection(HBaseConfiguration.create());
            userDao = new UserDao(connection);
            examenDao = new ExamenDao(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Produces
    @ApplicationScoped
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Produces
    @Dependent
    @Named("userDao")
    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Produces
    @Dependent
    @Named("examenDao")
    public ExamenDao getExamenDao() {
        return examenDao;
    }

    public void setExamenDao(ExamenDao examenDao) {
        this.examenDao = examenDao;
    }
}
