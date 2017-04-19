package org.javaleo.grandpa.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "blacklist_expression")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "blacklist_expression_sq", sequenceName = "blacklist_expression_seq", allocationSize = 1, initialValue = 1)
public class BlackListExpression implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private ScriptType scriptType;
	private Boolean regexp;
	private String content;

	@Id
	@Column(name = "black_expr_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blacklist_expression_sq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "script_type", length = 20, nullable = false)
	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	@NotNull
	@Column(name = "regexp", nullable = false)
	public Boolean getRegexp() {
		return regexp;
	}

	public void setRegexp(Boolean regexp) {
		this.regexp = regexp;
	}

	@NotNull
	@Column(name = "content", length = 120, nullable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((regexp == null) ? 0 : regexp.hashCode());
		result = prime * result + ((scriptType == null) ? 0 : scriptType.hashCode());
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
		BlackListExpression other = (BlackListExpression) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (regexp == null) {
			if (other.regexp != null)
				return false;
		} else if (!regexp.equals(other.regexp))
			return false;
		if (scriptType != other.scriptType)
			return false;
		return true;
	}

}
