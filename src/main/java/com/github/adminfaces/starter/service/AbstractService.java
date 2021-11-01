package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractService<T> {
    private Class<T> entityClass;
    private String collection;
    public AbstractService(Class<User> userClass) {
        this.entityClass = entityClass;
    }

    // inject Dao
    protected abstract MongoCollection<Document> getCollection();
    public T findBy(String criteria, String value) {
        // use dao to get data from database
        return null;
    }

    public void create(T entity) {
    }

    public void edit(T entity) {
    }

    public void remove(T entity) {
    }

    public List<T> findAll() {
        return (List<T>) getCollection().find().into(new ArrayList<>());
    }

    public int count() {
        return 0;
    }
}
