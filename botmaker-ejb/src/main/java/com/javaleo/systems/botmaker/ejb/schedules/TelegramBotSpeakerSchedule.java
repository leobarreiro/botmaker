package com.javaleo.systems.botmaker.ejb.schedules;

import java.io.Serializable;

import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Local
@Stateless
public class TelegramBotSpeakerSchedule implements Serializable {

	@Inject
	private ManagerUtils managerUtils;

	@Schedules({ @Schedule(dayOfWeek = "*", hour = "*", minute = "*", second = "00", persistent = false), @Schedule(dayOfWeek = "*", hour = "*", minute = "*", second = "20", persistent = false),
			@Schedule(dayOfWeek = "*", hour = "*", minute = "*", second = "40", persistent = false) })
	public void speakWithUsers() {

	}

}
