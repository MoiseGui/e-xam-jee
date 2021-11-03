package com.github.adminfaces.starter.infra.dao;

import com.github.adminfaces.starter.model.User;
import com.mongodb.WriteResult;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import javax.inject.Inject;
import java.io.Serializable;

public interface AbstractDao<T, ID extends Serializable> {
        Datastore datastore = null;

        default Key<T> create(T entity){
            return datastore.save(entity);
        }

        default T read(ID id){
            return datastore.get(getObjectClass(), id);
        }

        default UpdateResults update(T entity, UpdateOperations<T> operations){
            return datastore.update(entity, operations);
        }

//        UpdateResults update(Query<T> query, UpdateOperations<T> operations);

        default WriteResult delete(T entity){
            return datastore.delete(entity);
        }

        default UpdateOperations<T> createOperations(){
            return datastore.createUpdateOperations(getObjectClass());
        }

        default Class<T> getObjectClass(){
            return null;
        }


}
