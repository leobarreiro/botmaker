package org.javaleo.grandpa.ejb.entities.bot;

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

import org.javaleo.grandpa.ejb.entities.EntityUtils;
import org.javaleo.grandpa.ejb.enums.AnswerType;
import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "question")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "question_sq", sequenceName = "question_seq", initialValue = 1, allocationSize = 1)
public class Question implements IEntityBasic, Comparable<Question> {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String instruction;
	private AnswerType answerType;
	private Validator validator;
	private String errorFormatMessage;
	private String successMessage;
	private String varName;
	private Boolean processAnswer;
	private Script postScript;
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

	@ManyToOne(optional = true)
	@JoinColumn(name = "validator_id", referencedColumnName = "validator_id", foreignKey = @ForeignKey(name = "fk_question_validator"), nullable = true)
	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
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

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH })
	@JoinColumn(name = "post_script_id", referencedColumnName = "script_id", nullable = true)
	public Script getPostScript() {
		return postScript;
	}

	public void setPostScript(Script postScript) {
		this.postScript = postScript;
	}

	@Column(name = "order_number", nullable = false)
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@ManyToOne(optional = false, cascade = { CascadeType.REFRESH })
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
		result = prime * result + ((errorFormatMessage == null) ? 0 : errorFormatMessage.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((instruction == null) ? 0 : instruction.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((postScript == null) ? 0 : postScript.hashCode());
		result = prime * result + ((processAnswer == null) ? 0 : processAnswer.hashCode());
		result = prime * result + ((successMessage == null) ? 0 : successMessage.hashCode());
		result = prime * result + ((validator == null) ? 0 : validator.hashCode());
		result = prime * result + ((varName == null) ? 0 : varName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Question other = (Question) obj;
		if (answerType != other.answerType) return false;
		if (command == null) {
			if (other.command != null) return false;
		} else if (!command.equals(other.command)) return false;
		if (errorFormatMessage == null) {
			if (other.errorFormatMessage != null) return false;
		} else if (!errorFormatMessage.equals(other.errorFormatMessage)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (instruction == null) {
			if (other.instruction != null) return false;
		} else if (!instruction.equals(other.instruction)) return false;
		if (order == null) {
			if (other.order != null) return false;
		} else if (!order.equals(other.order)) return false;
		if (postScript == null) {
			if (other.postScript != null) return false;
		} else if (!postScript.equals(other.postScript)) return false;
		if (processAnswer == null) {
			if (other.processAnswer != null) return false;
		} else if (!processAnswer.equals(other.processAnswer)) return false;
		if (successMessage == null) {
			if (other.successMessage != null) return false;
		} else if (!successMessage.equals(other.successMessage)) return false;
		if (validator == null) {
			if (other.validator != null) return false;
		} else if (!validator.equals(other.validator)) return false;
		if (varName == null) {
			if (other.varName != null) return false;
		} else if (!varName.equals(other.varName)) return false;
		return true;
	}

}
