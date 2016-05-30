package com.javaleo.systems.botmaker.ejb.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = EntityUtils.COMMAND)
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "command_seq", sequenceName = "command_seq", initialValue = 1, allocationSize = 1)
public class Command implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String key;
	private String shortDescription;
	private String welcomeMessage;
	private Bot bot;
	private Boolean active;
	private List<Question> questions;
	private Boolean postProcess;
	private Script postScript;

	@Id
	@Override
	@Column(name = "command_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "command_seq")
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

	@OneToMany(mappedBy = "command", cascade = { CascadeType.REMOVE, CascadeType.REFRESH })
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	@Column(name = "post_process")
	public Boolean getPostProcess() {
		return postProcess;
	}

	public void setPostProcess(Boolean postProcess) {
		this.postProcess = postProcess;
	}

	@OneToOne(mappedBy = "command", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH })
	public Script getPostScript() {
		return postScript;
	}

	public void setPostScript(Script postScript) {
		this.postScript = postScript;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((bot == null) ? 0 : bot.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((postProcess == null) ? 0 : postProcess.hashCode());
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
		result = prime * result + ((shortDescription == null) ? 0 : shortDescription.hashCode());
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
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
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
		if (postProcess == null) {
			if (other.postProcess != null)
				return false;
		} else if (!postProcess.equals(other.postProcess))
			return false;
		if (questions == null) {
			if (other.questions != null)
				return false;
		} else if (!questions.equals(other.questions))
			return false;
		if (shortDescription == null) {
			if (other.shortDescription != null)
				return false;
		} else if (!shortDescription.equals(other.shortDescription))
			return false;
		if (welcomeMessage == null) {
			if (other.welcomeMessage != null)
				return false;
		} else if (!welcomeMessage.equals(other.welcomeMessage))
			return false;
		return true;
	}

}
