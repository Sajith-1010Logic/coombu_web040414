package com.coombu.photobook.service;

import com.coombu.photobook.model.Schedule;

public interface IScheduleService {

	public abstract Schedule getCurrentGroupSchedule(long eventId);

	public abstract Schedule saveOrUpdateSchedule(Schedule schedule);

}
