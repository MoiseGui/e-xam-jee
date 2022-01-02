package com.github.adminfaces.starter.infra.dao;

import com.github.adminfaces.starter.model.User;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class UserDao {
    @Inject
    @RequestScoped
    Datastore datastore;

    public long getCount(){
        return datastore.getCount(User.class);
    }

    public Key<User> create(User entity){
        return datastore.save(entity);
    }

    public User read(ObjectId id){
        return datastore.get(User.class, id);
    }

    public List<User> findAll(){
        return datastore.find(User.class).asList();
    }

    public User findById(String id) {
        return datastore.get(User.class, new ObjectId(id));
    }

    public User findByEmail(String email){
        return datastore.find(User.class, "email", email).get();
    }
    
    public User findByNom(String nom){
        return datastore.find(User.class, "nom", nom).get();
    }
    public UpdateResults update(User entity, UpdateOperations<User> operations){
        return datastore.update(entity, operations);
    }

    public WriteResult delete(User entity){
        return datastore.delete(entity);
    }

    public UpdateOperations<User> createOperations(){
        return datastore.createUpdateOperations(User.class);
    }

}
