package com.github.adminfaces.starter.util;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import java.io.Serializable;

@Singleton
@Startup
public class MongoConnexion implements Serializable {

    private static final long serialVersionUID = 4575125557867859065L;

    private final String connectionString = "mongodb+srv://e-xam:GFor1FraPS9OGHhw@cluster0.paraj.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    private static MongoClient mongo;
    private static Datastore datastore;


    @PostConstruct
    public void init() {
        if ((mongo == null) && (datastore == null)) {
            mongo = new MongoClient("localhost:27017");
            Morphia morphia = new Morphia();
            datastore = morphia.createDatastore(mongo, "mycustomer");
            morphia.getMapper().getConverters().addConverter(BigDecimalConverter.class);
        }
    }

    @Produces
    @RequestScoped
    public Datastore getDatastore() {
        return datastore;
    }

    public MongoClient getMongoClient() {
        return mongo;
    }

    public void closeConnection() {
        if (mongo != null) {
            mongo.close();
            mongo = null;
        }
        datastore = null;
    }
}
