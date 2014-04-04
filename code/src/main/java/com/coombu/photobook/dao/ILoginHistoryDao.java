package com.coombu.photobook.dao;

import java.util.Date;
import java.util.List;

import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.LoginHistory;

public interface ILoginHistoryDao extends IDao
{

	public abstract void save(LoginHistory transientInstance);
	
	public abstract void saveOrUpdate(LoginHistory transientInstance);

	public abstract void delete(LoginHistory persistentInstance);

	public abstract LoginHistory merge(LoginHistory detachedInstance);

	public abstract LoginHistory findById(int id);

	public abstract List<EventSecurityUser> getLoggedInUsers(long eventId);
	
	public abstract List<LoginHistory> getAllHistoryFromOrNowAndToDate(
			long currentEventId, Date startDate, Date endDate);

}