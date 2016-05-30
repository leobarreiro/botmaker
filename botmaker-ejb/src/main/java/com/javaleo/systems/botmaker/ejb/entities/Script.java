package com.javaleo.systems.botmaker.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.libs.botgram.enums.ParseMode;
import org.javaleo.libs.jee.core.model.IEntityBasic;

import com.javaleo.systems.botmaker.ejb.enums.ScriptType;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = EntityUtils.SCRIPT)
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "script_seq", sequenceName = "script_seq", initialValue = 1, allocationSize = 1)
public class Script implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Boolean valid;
	private Boolean enabled;
	private ScriptType scriptType;
	private ParseMode parseMode;
	private String code;
	private Command command;
	private Question question;

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

	@Column(name = "code", columnDefinition = "text", nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@OneToOne(optional = true)
	@JoinColumn(name = "command_id", nullable = true, foreignKey = @ForeignKey(name = "fk_script_command"))
	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	@OneToOne(optional = true)
	@JoinColumn(name = "question_id", nullable = true, foreignKey = @ForeignKey(name = "fk_script_question"))
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}
