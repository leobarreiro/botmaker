package org.javaleo.grandpa.ejb.entities.price;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.grandpa.ejb.entities.EntityUtils;
import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "product")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "sq_product", sequenceName = "seq_product", allocationSize = 1, initialValue = 1)
public class Product implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String description;
	private Measure measure;
	private Double cost;
	private Double price;
	private Boolean active;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_product")
	@Column(name = "product_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", length = 60, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 120, nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name = "measure_id", referencedColumnName = "measure_id")
	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	@Column(name = "cost")
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Column(name = "price")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "active")

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((measure == null) ? 0 : measure.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Product other = (Product) obj;
		if (active == null) {
			if (other.active != null) return false;
		} else if (!active.equals(other.active)) return false;
		if (cost == null) {
			if (other.cost != null) return false;
		} else if (!cost.equals(other.cost)) return false;
		if (description == null) {
			if (other.description != null) return false;
		} else if (!description.equals(other.description)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (measure == null) {
			if (other.measure != null) return false;
		} else if (!measure.equals(other.measure)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (price == null) {
			if (other.price != null) return false;
		} else if (!price.equals(other.price)) return false;
		return true;
	}

}
