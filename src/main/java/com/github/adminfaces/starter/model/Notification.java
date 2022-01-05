package com.github.adminfaces.starter.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;

import com.github.adminfaces.starter.util.PersistentEntity;

@Entity(value = "examens", noClassnameStored = true)
public class Notification extends PersistentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String email;
	private String message;
	private Boolean status;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Notification() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
