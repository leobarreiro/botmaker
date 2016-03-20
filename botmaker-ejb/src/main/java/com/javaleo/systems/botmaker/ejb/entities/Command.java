package com.javaleo.systems.botmaker.ejb.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = EntityUtils.COMMAND)
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "command_sq", sequenceName = "command_seq", initialValue = 1, allocationSize = 1)
public class Command implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String key;
	private String shortDescription;
	private String welcomeMessage;
	private Bot bot;
	private Boolean active;
	private List<Question> questions;

	@Override
	@Id
	@Column(name = "command_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "command_sq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "key", nullable = false)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "short_description", length = 255)
	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	@Column(name = "welcome_message", length = 255)
	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "bot_id", referencedColumnName = "bot_id", foreignKey = @ForeignKey(name = "fk_command_bot"))
	public Bot getBot() {
		return bot;
	}

	public void setBot(Bot bot) {
		this.bot = bot;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@OneToMany(mappedBy = "command")
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bot == null) ? 0 : bot.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((welcomeMessage == null) ? 0 : welcomeMessage.hashCode());
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
		Command other = (Command) obj;
		if (bot == null) {
			if (other.bot != null)
				return false;
		} else if (!bot.equals(other.bot))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (welcomeMessage == null) {
			if (other.welcomeMessage != null)
				return false;
		} else if (!welcomeMessage.equals(other.welcomeMessage))
			return false;
		return true;
	}

}
