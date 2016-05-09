package com.javaleo.systems.botmaker.ejb.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.javaleo.libs.botgram.enums.ParseMode;
import org.javaleo.libs.jee.core.model.IEntityBasic;

import com.javaleo.systems.botmaker.ejb.enums.AnswerType;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "question")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "question_sq", sequenceName = "question_seq", initialValue = 1, allocationSize = 1)
public class Question implements IEntityBasic, Comparable<Question> {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String instruction;
	private AnswerType answerType;
	private Validator validator;
	private String options;
	private String errorFormatMessage;
	private String successMessage;
	private String varName;
	private Boolean processAnswer;
	private ParseMode parseMode;
	private ScriptType scriptType;
	private String postProcessScript;
	private Integer order;
	private Command command;

	@Override
	@Id
	@Column(name = "question_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_sq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "instruction", nullable = false)
	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "answer_type", length = 20)
	public AnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
	}

	@NotNull
	@ManyToOne()
	@JoinColumn(name = "validator_id", referencedColumnName = "validator_id", foreignKey = @ForeignKey(name = "fk_question_validator"), nullable = false)
	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@Column(name = "answer_options", length = 255, nullable = true)
	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	@Column(name = "error_message", length = 255)
	public String getErrorFormatMessage() {
		return errorFormatMessage;
	}

	public void setErrorFormatMessage(String errorFormatMessage) {
		this.errorFormatMessage = errorFormatMessage;
	}

	@Column(name = "success_message", length = 255)
	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	@Column(name = "var_name", length = 30, nullable = false)
	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	@Column(name = "process_answer")
	public Boolean getProcessAnswer() {
		return processAnswer;
	}

	public void setProcessAnswer(Boolean processAnswer) {
		this.processAnswer = processAnswer;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "parse_mode", length = 20, nullable = true)
	public ParseMode getParseMode() {
		return parseMode;
	}

	public void setParseMode(ParseMode parseMode) {
		this.parseMode = parseMode;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "script_type", length = 30)
	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	@Column(name = "post_process_script", columnDefinition = "text", nullable = true)
	public String getPostProcessScript() {
		return postProcessScript;
	}

	public void setPostProcessScript(String postProcessScript) {
		this.postProcessScript = postProcessScript;
	}

	@Column(name = "order_number", nullable = false)
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@ManyToOne(optional = false, cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "command_id", referencedColumnName = "command_id", foreignKey = @ForeignKey(name = "fk_question_command"))
	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	@Override
	public int compareTo(Question o) {
		if (this.getOrder() != null && o.getOrder() != null) {
			if (this.getOrder() < o.getOrder()) {
				return -1;
			} else if (this.getOrder().equals(o.getOrder())) {
				return 0;
			} else {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answerType == null) ? 0 : answerType.hashCode());
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((instruction == null) ? 0 : instruction.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((parseMode == null) ? 0 : parseMode.hashCode());
		result = prime * result + ((validator == null) ? 0 : validator.hashCode());
		result = prime * result + ((varName == null) ? 0 : varName.hashCode());
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
		Question other = (Question) obj;
		if (answerType != other.answerType)
			return false;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (instruction == null) {
			if (other.instruction != null)
				return false;
		} else if (!instruction.equals(other.instruction))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (parseMode != other.parseMode)
			return false;
		if (validator == null) {
			if (other.validator != null)
				return false;
		} else if (!validator.equals(other.validator))
			return false;
		if (varName == null) {
			if (other.varName != null)
				return false;
		} else if (!varName.equals(other.varName))
			return false;
		return true;
	}

}
