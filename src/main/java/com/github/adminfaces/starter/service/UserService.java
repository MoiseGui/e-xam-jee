package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserService   implements Serializable {

    @Inject
    private MongoDatabase database;
    @Inject
    private MongoCollection<Document> collection;


    public int count(){
        return (int) collection.countDocuments();
    }
    public User findBylogin(String login) {
        return null;
    }

    public int signIn(String login, String password) {
        return 0;
    }

    protected MongoCollection<Document> getCollection() {
        return collection;
    }
}
