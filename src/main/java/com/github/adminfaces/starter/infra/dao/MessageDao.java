package com.github.adminfaces.starter.infra.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.github.adminfaces.starter.model.Notification;
import com.mongodb.WriteResult;

@Stateless
public class MessageDao {
	@Inject
	Datastore datastore;

	public Key<Notification> create(Notification entity) {
		return datastore.save(entity);
	}

	public long getCount() {
		return datastore.getCount(Notification.class);
	}

	public Notification read(ObjectId id) {
		return datastore.get(Notification.class, id);
	}

	public List<Notification> findAll() {
		return datastore.find(Notification.class).asList();
	}

	public Notification findById(String id) {
		return datastore.get(Notification.class, new ObjectId(id));
	}

	public Notification findByEmail(String email) {
		return datastore.find(Notification.class, "email", email).get();
	}

	public UpdateResults update(Notification entity, UpdateOperations<Notification> operations) {
		return datastore.update(entity, operations);
	}

	public WriteResult delete(Notification entity) {
		return datastore.delete(entity);
	}

	public UpdateOperations<Notification> createOperations() {
		return datastore.createUpdateOperations(Notification.class);
	}

}
