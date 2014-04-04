package com.coombu.photobook.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.Schedule;

// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class Schedule.
 * 
 * @see com.coombu.photobook.model.Schedule
 * @author Fekade Aytaged
 */
@Repository
public class ScheduleDao extends HibernateBaseDao implements IScheduleDao {

	private static final Logger log = LoggerFactory
			.getLogger(ScheduleDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IScheduleDao#save(com.coombu.photobook.model
	 * .Schedule)
	 */
	@Override
	public void save(Schedule transientInstance) {
		log.debug("persisting Schedule instance");
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
	 * com.coombu.photobook.dao.IScheduleDao#delete(com.coombu.photobook.model
	 * .Schedule)
	 */
	@Override
	public void delete(Schedule persistentInstance) {
		log.debug("removing Schedule instance");
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
	 * com.coombu.photobook.dao.IScheduleDao#merge(com.coombu.photobook.model
	 * .Schedule)
	 */
	@Override
	public Schedule merge(Schedule detachedInstance) {
		log.debug("merging Schedule instance");
		try {
			Schedule result = (Schedule) super.merge(detachedInstance);
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
	 * @see com.coombu.photobook.dao.IScheduleDao#findById(int)
	 */
	public Schedule findById(int id) {
		log.debug("getting Schedule instance with id: " + id);
		try {
			Schedule instance = (Schedule) super.findById(Schedule.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public Schedule findScheduleByEventId(long eventId) {
		Criteria cri = getCurrentSession().createCriteria(Schedule.class);
		return (Schedule) cri.add(Restrictions.eq("event.eventId", eventId))
				.uniqueResult();
	}
}
