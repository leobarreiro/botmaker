package org.javaleo.grandpa.ejb.filters;

import java.io.Serializable;

public class PhotoFilter implements Serializable {

	private static final long serialVersionUID = -6881167307992073386L;

	private String name;
	private String description;
	private String mimeType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
