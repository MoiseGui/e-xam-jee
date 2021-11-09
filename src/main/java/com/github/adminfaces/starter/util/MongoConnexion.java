package com.github.adminfaces.starter.util;

import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.Question;
import com.github.adminfaces.starter.model.User;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Singleton
@Startup
public class MongoConnexion implements Serializable {

    private static final long serialVersionUID = 4575125557867859065L;

    private final String connectionString = "mongodb+srv://e-xam:GFor1FraPS9OGHhw@cluster0.paraj.mongodb.net/e-xam?retryWrites=true&w=majority";
    private static MongoClient mongo;
    private static Datastore datastore;
    private List<User> allUsers;

    @PostConstruct
    public void init() {
        if ((mongo == null) && (datastore == null)) {
            mongo = new MongoClient("localhost:27017");
            Morphia morphia = new Morphia();
            datastore = morphia.createDatastore(mongo, "e-xam");
            morphia.getMapper().getConverters().addConverter(BigDecimalConverter.class);
            allUsers = datastore.createQuery(User.class).asList();
            System.out.println("********************************");
            allUsers.forEach(System.out::println);
            System.out.println("********************************");
            System.out.println("insert exams");
            String random = String.valueOf((int) Math.floor(Math.random() * 10));
            Examen examen1 = new Examen();
            examen1.setLibelle("libelle" + random);
            examen1.setDateDebut(new Date());
            examen1.setDateFin(new Date());
            System.out.println("create Questions");
            Question question = new Question();
            question.setOrdre(1);
            question.setImage("image");
            question.setDateCreation(new Date());
            question.setPoints(10.0);
            question.setTitre("titre 1");
            examen1.setQuestions(Arrays.asList(question));
            datastore.save(examen1);
            System.out.println("********************************");
        }
    }

    @Produces
    @RequestScoped
    public Datastore getDatastore() {
        return datastore;
    }

    @Produces
    @RequestScoped
    public List<User> getAllUsers() {
        return allUsers;
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
