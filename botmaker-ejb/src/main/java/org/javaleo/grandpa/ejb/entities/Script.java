package org.javaleo.grandpa.ejb.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.libs.botgram.enums.ParseMode;
import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = EntityUtils.SCRIPT)
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "script_seq", sequenceName = "script_seq", initialValue = 1, allocationSize = 1)
public class Script implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private User author;
	private Date created;
	private Date modified;
	private Boolean valid;
	private Boolean enabled;
	private ScriptType scriptType;
	private ParseMode parseMode;
	private String name;
	private String description;
	private Boolean generic;
	private String code;
	private Command command;
	private Question question;
	private Validator validator;
	private Script genericScript;
	private Boolean publicUse;

	@Id
	@Override
	@Column(name = "script_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "script_seq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "author_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "fk_script_user_author"))
	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified")
	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	@Column(name = "valid")
	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	@Column(name = "enabled")
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "script_type", length = 20)
	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "parse_mode", length = 20)
	public ParseMode getParseMode() {
		return parseMode;
	}

	public void setParseMode(ParseMode parseMode) {
		this.parseMode = parseMode;
	}

	@Column(name = "name", length = 40, nullable = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 255, nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "generic", nullable = true)
	public Boolean getGeneric() {
		return generic;
	}

	public void setGeneric(Boolean generic) {
		this.generic = generic;
	}

	@Column(name = "code", columnDefinition = "text", nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@OneToOne(fetch = FetchType.EAGER, optional = true, mappedBy = "postScript", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "command_id", referencedColumnName = "command_id", nullable = true, foreignKey = @ForeignKey(name = "fk_script_command"))
	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	@OneToOne(fetch = FetchType.EAGER, optional = true, mappedBy = "postScript")
	@JoinColumn(name = "question_id", referencedColumnName = "question_id", nullable = true, foreignKey = @ForeignKey(name = "fk_script_question"))
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@OneToOne(fetch = FetchType.EAGER, optional = true, mappedBy = "script", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "validator_id", referencedColumnName = "validator_id", nullable = true, foreignKey = @ForeignKey(name = "fk_script_validator"))
	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "generic_script", referencedColumnName = "script_id", foreignKey = @ForeignKey(name = "fk_generic_script"))
	public Script getGenericScript() {
		return genericScript;
	}

	public void setGenericScript(Script genericScript) {
		this.genericScript = genericScript;
	}

	@Column(name = "public_use", nullable = true)
	public Boolean getPublicUse() {
		return publicUse;
	}

	public void setPublicUse(Boolean publicUse) {
		this.publicUse = publicUse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((generic == null) ? 0 : generic.hashCode());
		result = prime * result + ((genericScript == null) ? 0 : genericScript.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parseMode == null) ? 0 : parseMode.hashCode());
		result = prime * result + ((publicUse == null) ? 0 : publicUse.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((scriptType == null) ? 0 : scriptType.hashCode());
		result = prime * result + ((valid == null) ? 0 : valid.hashCode());
		result = prime * result + ((validator == null) ? 0 : validator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Script other = (Script) obj;
		if (author == null) {
			if (other.author != null) return false;
		} else if (!author.equals(other.author)) return false;
		if (code == null) {
			if (other.code != null) return false;
		} else if (!code.equals(other.code)) return false;
		if (command == null) {
			if (other.command != null) return false;
		} else if (!command.equals(other.command)) return false;
		if (created == null) {
			if (other.created != null) return false;
		} else if (!created.equals(other.created)) return false;
		if (description == null) {
			if (other.description != null) return false;
		} else if (!description.equals(other.description)) return false;
		if (enabled == null) {
			if (other.enabled != null) return false;
		} else if (!enabled.equals(other.enabled)) return false;
		if (generic == null) {
			if (other.generic != null) return false;
		} else if (!generic.equals(other.generic)) return false;
		if (genericScript == null) {
			if (other.genericScript != null) return false;
		} else if (!genericScript.equals(other.genericScript)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (modified == null) {
			if (other.modified != null) return false;
		} else if (!modified.equals(other.modified)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (parseMode != other.parseMode) return false;
		if (publicUse == null) {
			if (other.publicUse != null) return false;
		} else if (!publicUse.equals(other.publicUse)) return false;
		if (question == null) {
			if (other.question != null) return false;
		} else if (!question.equals(other.question)) return false;
		if (scriptType != other.scriptType) return false;
		if (valid == null) {
			if (other.valid != null) return false;
		} else if (!valid.equals(other.valid)) return false;
		if (validator == null) {
			if (other.validator != null) return false;
		} else if (!validator.equals(other.validator)) return false;
		return true;
	}

}
