package com.javaleo.systems.botmaker.ejb.filters;

import java.io.Serializable;
import java.util.Date;

import com.javaleo.systems.botmaker.ejb.entities.User;

public class GalleryFilter implements Serializable {

	private static final long serialVersionUID = 6318210783562261163L;

	private String name;
	private User user;
	private Date createdStart;
	private Date createdEnd;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreatedStart() {
		return createdStart;
	}

	public void setCreatedStart(Date createdStart) {
		this.createdStart = createdStart;
	}

	public Date getCreatedEnd() {
		return createdEnd;
	}

	public void setCreatedEnd(Date createdEnd) {
		this.createdEnd = createdEnd;
	}

}
