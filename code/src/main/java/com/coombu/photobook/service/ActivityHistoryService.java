package com.coombu.photobook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.dao.IActivityHistoryDao;
import com.coombu.photobook.model.ActivityHistory;

@Service
@Transactional
public class ActivityHistoryService implements IActivityHistoryService {

	@Autowired
	private IActivityHistoryDao activityDao;

	public IActivityHistoryDao getActivityDao() {
		return activityDao;
	}

	public void setActivityDao(IActivityHistoryDao activityDao) {
		this.activityDao = activityDao;
	}

	@Override
	public List<ActivityHistory> getAllMyLatestActivities(int startFrom,
			int maxCount, long securityUserId, long eventId) {

		return activityDao.getAllMyLatestActivities(startFrom, maxCount,
				securityUserId, eventId);
	}

	@Override
	public List<ActivityHistory> getAllLatestGroupActivities(int startFrom,
			int maxCount, long securityUserId, long eventId) {

		return activityDao.getAllLatestGroupActivities(startFrom, maxCount,
				securityUserId, eventId);
	}

	@Override
	public long getCountOfAllMyActivites(long securityUserId, long eventId) {
		// TODO Auto-generated method stub
		return activityDao.getCountOfAllMyActivites(securityUserId, eventId);
	}

	@Override
	public long getCountOfAllActivitesOfGroup(long securityUserId, long eventId) {
		// TODO Auto-generated method stub
		return activityDao.getCountOfAllActivitesOfGroup(securityUserId,
				eventId);
	}

	/*@Override
	public boolean createNewActivity(long eventId, int activityTypeId,
			long initiatorUserId, long componentId, long impactedUserId,
			long createdBy) {
		
		ActivityHistory history=new ActivityHistory();
		
		return false;
	}*/

}
