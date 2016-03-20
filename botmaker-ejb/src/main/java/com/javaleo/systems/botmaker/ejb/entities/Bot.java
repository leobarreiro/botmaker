package com.javaleo.systems.botmaker.ejb.entities;

import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.javaleo.libs.jee.core.model.IEntityBasic;

import com.javaleo.systems.botmaker.ejb.enums.BotType;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = EntityUtils.BOT)
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "bot_sq", sequenceName = "bot_seq", allocationSize = 1, initialValue = 1)
public class Bot implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String token;
	private BotType botType;
	private Boolean active;
	private String closedBotMessage;
	private String unknownCommadMessage;
	private Company company;
	private List<Command> commands;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bot_sq")
	@Column(name = "bot_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", length = 60)
	@NotNull(message = "Name of Bot may not be null")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "token")
	@NotNull(message = "Token of Bot may not be null")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "bot_type", length = 20)
	@NotNull(message = "Type of Bot may not be null")
	public BotType getBotType() {
		return botType;
	}

	public void setBotType(BotType botType) {
		this.botType = botType;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "closed_message", length = 255)
	public String getClosedBotMessage() {
		return closedBotMessage;
	}

	public void setClosedBotMessage(String closedBotMessage) {
		this.closedBotMessage = closedBotMessage;
	}

	@Column(name = "unknown_message", length = 255)
	public String getUnknownCommadMessage() {
		return unknownCommadMessage;
	}

	public void setUnknownCommadMessage(String unknownCommadMessage) {
		this.unknownCommadMessage = unknownCommadMessage;
	}

	// TODO : remove optional = true after applying in database update mode
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "company_id", referencedColumnName = "company_id", foreignKey = @ForeignKey(name = "fk_bot_company"))
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@OneToMany(mappedBy = "bot")
	public List<Command> getCommands() {
		return commands;
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((botType == null) ? 0 : botType.hashCode());
		result = prime * result + ((closedBotMessage == null) ? 0 : closedBotMessage.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((unknownCommadMessage == null) ? 0 : unknownCommadMessage.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bot other = (Bot) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (botType != other.botType)
			return false;
		if (closedBotMessage == null) {
			if (other.closedBotMessage != null)
				return false;
		} else if (!closedBotMessage.equals(other.closedBotMessage))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (unknownCommadMessage == null) {
			if (other.unknownCommadMessage != null)
				return false;
		} else if (!unknownCommadMessage.equals(other.unknownCommadMessage))
			return false;
		return true;
	}

}
