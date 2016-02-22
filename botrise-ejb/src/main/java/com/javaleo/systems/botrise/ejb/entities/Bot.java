package com.javaleo.systems.botrise.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.javaleo.libs.jee.core.model.IEntityBasic;

import com.javaleo.systems.botrise.ejb.enums.BotType;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = EntityUtils.BOT)
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "bot_sq", sequenceName = "bot_seq", allocationSize = 1, initialValue = 1)
public class Bot implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String token;
	private BotType botType;
	private String unabledMessage;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bot_sq")
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name")
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

	@Lob
	@Column(name = "unabled_message", columnDefinition = "text", length = 500, nullable = true)
	public String getUnabledMessage() {
		return unabledMessage;
	}

	public void setUnabledMessage(String unabledMessage) {
		this.unabledMessage = unabledMessage;
	}

}
