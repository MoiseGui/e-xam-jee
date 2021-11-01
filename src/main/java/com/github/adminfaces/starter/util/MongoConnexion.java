package com.github.adminfaces.starter.util;

import com.github.adminfaces.starter.model.Car;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MongoConnexion {
    private final String connectionString = "mongodb+srv://e-xam:GFor1FraPS9OGHhw@cluster0.paraj.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @PostConstruct
    public void init() {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            database = mongoClient.getDatabase("e-xam");
            collection = database.getCollection("users");
            System.out.println("collection.toString() = " + collection.toString());
            System.out.println("**************************************************");
            System.out.println("Connected to Database : "+database.getName());
            System.out.println("**************************************************");
        }
    }
    @Produces
    public MongoCollection<Document> getCollection() {
        return collection;
    }

    @Produces
    public MongoDatabase getDatabase() {
        return database;
    }
}
