package org.javaleo.grandpa.ejb.pojos;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DialogContextVar implements Serializable {

	private static final long serialVersionUID = 1L;

	public DialogContextVar(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public DialogContextVar(String name, String value, String description) {
		this.name = name;
		this.value = value;
		this.description = description;
	}

	@Expose
	@SerializedName("name")
	private String name;

	@Expose
	@SerializedName("value")
	private String value;

	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DialogContextVar other = (DialogContextVar) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
