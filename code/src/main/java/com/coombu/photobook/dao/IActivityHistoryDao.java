package com.coombu.photobook.dao;

import java.util.List;

import com.coombu.photobook.model.ActivityHistory;

public interface IActivityHistoryDao {

	public abstract void save(ActivityHistory transientInstance);

	public abstract void delete(ActivityHistory persistentInstance);

	public abstract ActivityHistory merge(ActivityHistory detachedInstance);

	public abstract ActivityHistory findById(int id);

	public List<ActivityHistory> getAllMyLatestActivities(int startFrom,
			int maxCount, long securityUserId, long eventId);

	public List<ActivityHistory> getAllLatestGroupActivities(int startFrom,
			int maxCount, long securityUserId, long eventId);

	public long getCountOfAllMyActivites(long securityUserId, long eventId);

	public long getCountOfAllActivitesOfGroup(long securityUserId, long eventId);
}
