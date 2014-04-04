package com.coombu.photobook.dao;

import java.util.List;

import javassist.convert.Transformer;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.ActivityHistory;

@Repository
public class ActivityHistoryDao extends HibernateBaseDao implements
		IActivityHistoryDao {

	private static final Logger log = LoggerFactory
			.getLogger(ActivityHistoryDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IActivityHistoryDao#save(com.coombu.photobook
	 * .model.ActivityHistory)
	 */
	@Override
	public void save(ActivityHistory transientInstance) {
		log.debug("persisting ActivityHistory instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IActivityHistoryDao#delete(com.coombu.photobook
	 * .model.ActivityHistory)
	 */
	@Override
	public void delete(ActivityHistory persistentInstance) {
		log.debug("removing ActivityHistory instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IActivityHistoryDao#merge(com.coombu.photobook
	 * .model.ActivityHistory)
	 */
	@Override
	public ActivityHistory merge(ActivityHistory detachedInstance) {
		log.debug("merging ActivityHistory instance");
		try {
			ActivityHistory result = (ActivityHistory) super
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coombu.photobook.dao.IActivityHistoryDao#findById(int)
	 */
	public ActivityHistory findById(int id) {
		log.debug("getting ActivityHistory instance with id: " + id);
		try {
			ActivityHistory instance = (ActivityHistory) super.findById(
					ActivityHistory.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<ActivityHistory> getAllMyLatestActivities(int startFrom,
			int maxCount, long securityUserId, long eventId) {
		List<ActivityHistory> activityList = null;

		log.debug("fetching all myactivities list");

		Criteria criteria = getCurrentSession().createCriteria(
				ActivityHistory.class);

		criteria.addOrder(Order.desc("createTimestamp"));

		Criterion criterion = Restrictions
				.disjunction()
				.add(Restrictions
						.eq("initiator.securityUserId", securityUserId))
				.add(Restrictions.eq("impactedUser.securityUserId",
						securityUserId));

		criteria.add(criterion);

		criteria.add(Restrictions.eq("event.eventId", eventId));

		criteria.setFirstResult(startFrom);

		criteria.setMaxResults(maxCount);

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		activityList = criteria.list();

		if (activityList != null && !activityList.isEmpty()) {
			log.debug("actvities found the total list size is:" + activityList);
		} else {
			log.debug("my activity List is empty!");
		}

		return activityList;
	}

	@Override
	public List<ActivityHistory> getAllLatestGroupActivities(int startFrom,
			int maxCount, long securityUserId, long eventId) {
		List<ActivityHistory> activityList = null;

		log.debug("fetching all latest group activities list");

		Criteria criteria = getCurrentSession().createCriteria(
				ActivityHistory.class);

		criteria.addOrder(Order.desc("createTimestamp"));

		Criterion criterion = Restrictions
				.conjunction()
				.add(Restrictions
						.ne("initiator.securityUserId", securityUserId))
				.add(Restrictions.ne("impactedUser.securityUserId",
						securityUserId));

		criteria.add(criterion);

		criteria.add(Restrictions.eq("event.eventId", eventId));

		criteria.setFirstResult(startFrom);

		criteria.setMaxResults(maxCount);

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		activityList = criteria.list();

		if (activityList != null && !activityList.isEmpty()) {
			log.debug("actvities found for group. the list is:" + activityList);
		} else {
			log.debug("group activity List is empty!");
		}

		return activityList;
	}

	@Override
	public long getCountOfAllMyActivites(long securityUserId, long eventId) {
		Criteria criteria = getCurrentSession().createCriteria(
				ActivityHistory.class);
		log.debug("getting all my activities count!");
		long count = 0;
		Criterion criterion = Restrictions
				.disjunction()
				.add(Restrictions
						.eq("initiator.securityUserId", securityUserId))
				.add(Restrictions.eq("impactedUser.securityUserId",
						securityUserId));
		criteria.add(criterion);
		criteria.add(Restrictions.eq("event.eventId", eventId));
		count = (long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		log.debug("got all my activities count! here it is: " + count);
		return count;
	}

	@Override
	public long getCountOfAllActivitesOfGroup(long securityUserId, long eventId) {
		Criteria criteria = getCurrentSession().createCriteria(
				ActivityHistory.class);
		log.debug("getting all activities count of group!");
		long count = 0;
		Criterion criterion = Restrictions
				.conjunction()
				.add(Restrictions
						.ne("initiator.securityUserId", securityUserId))
				.add(Restrictions.ne("impactedUser.securityUserId",
						securityUserId));
		criteria.add(criterion);
		criteria.add(Restrictions.eq("event.eventId", eventId));
		count = (long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		log.debug("got all activities count of group! here it is: " + count);
		return count;
	}

	

}
