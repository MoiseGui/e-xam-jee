package com.github.adminfaces.starter.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.github.adminfaces.starter.infra.dao.MessageDao;
import com.github.adminfaces.starter.model.Notification;
import com.mongodb.WriteResult;

@Stateless
public class NotificationService implements Serializable {

	@Inject
	private MessageDao messageDao;

	public UpdateOperations<Notification> createOperations() {
		return messageDao.createOperations();
	}

	public Key<Notification> create(Notification entity) {
		return messageDao.create(entity);
	}

	public long getCount() {
		return messageDao.getCount();
	}

	public List<Notification> findAll() {
		return messageDao.findAll();
	}

	public Notification findById(String id) {
		return messageDao.findById(id);
	}

	public Notification findByEmail(String email) {
		return messageDao.findByEmail(email);
	}

	public UpdateResults update(Notification entity, UpdateOperations<Notification> operations) {
		Notification realMessage = findById(entity.getId());
		if (realMessage != null) {
			UpdateOperations<Notification> updateOperations = messageDao.createOperations();
			updateOperations.set("status", false);
			messageDao.update(entity, updateOperations);
		}
		return messageDao.update(entity, operations);
	}

	public WriteResult delete(Notification entity) {
		return messageDao.delete(entity);
	}

}
