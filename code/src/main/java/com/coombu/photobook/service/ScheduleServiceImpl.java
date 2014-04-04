package com.coombu.photobook.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.dao.IScheduleDao;
import com.coombu.photobook.model.Schedule;

@Service
@Transactional
public class ScheduleServiceImpl implements IScheduleService {

	@Autowired
	private IScheduleDao scheduleDao;

	private org.slf4j.Logger log = LoggerFactory
			.getLogger(ScheduleServiceImpl.class);

	public IScheduleDao getScheduleDao() {
		return scheduleDao;
	}

	public void setScheduleDao(IScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}

	@Override
	public Schedule getCurrentGroupSchedule(long eventId) {

		return scheduleDao.findScheduleByEventId(eventId);
	}

	@Override
	public Schedule saveOrUpdateSchedule(Schedule schedule) {

		return scheduleDao.merge(schedule);
	}
}
