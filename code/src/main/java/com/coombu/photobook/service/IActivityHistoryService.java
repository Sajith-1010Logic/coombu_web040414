package com.coombu.photobook.service;

import java.util.List;

import com.coombu.photobook.model.ActivityHistory;

public interface IActivityHistoryService {

	public List<ActivityHistory> getAllMyLatestActivities(int startFrom,
			int maxCount, long securityUserId, long eventId);

	public List<ActivityHistory> getAllLatestGroupActivities(int startFrom,
			int maxCount, long securityUserId, long eventId);

	public long getCountOfAllMyActivites(long securityUserId, long eventId);

	public long getCountOfAllActivitesOfGroup(long securityUserId, long eventId);

	/*public boolean createNewActivity(Event event, ActivityType activityTypeId,
			long initiatorUserId, long componentId, long impactedUserId,
			long createdBy);*/

}
