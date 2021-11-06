package com.github.adminfaces.starter.infra.dao;

import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.User;
import com.mongodb.WriteResult;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class ExamenDao {
    @Inject
    Datastore datastore;

    public Key<Examen> create(Examen entity) {
        return datastore.save(entity);
    }
    public long getCount(){
        return datastore.getCount(Examen.class);
    }
    public List<Examen> findAll() {
        return datastore.createQuery(Examen.class).asList();
    }
    public WriteResult delete(Examen entity){
        return datastore.delete(entity);
    }

}
