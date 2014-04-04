package com.coombu.photobook.dao;

import com.coombu.photobook.model.Schedule;

public interface IScheduleDao {

	public abstract void save(Schedule transientInstance);

	public abstract void delete(Schedule persistentInstance);

	public abstract Schedule merge(Schedule detachedInstance);

	public abstract Schedule findById(int id);

	public abstract Schedule findScheduleByEventId(long eventId);

}