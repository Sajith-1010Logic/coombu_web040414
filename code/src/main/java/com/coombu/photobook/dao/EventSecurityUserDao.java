package com.coombu.photobook.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.util.Constants.USER_STATUS_TYPE;

// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class EventSecurityUser.
 * 
 * @see com.coombu.photobook.model.EventSecurityUser
 * @author Fekade Aytaged
 */
@Repository
public class EventSecurityUserDao extends HibernateBaseDao implements
		IEventSecurityUserDao, Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory
			.getLogger(EventSecurityUserDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IEventSecurityUserDao#save(com.coombu.photobook
	 * .model.EventSecurityUser)
	 */
	@Override
	public void save(EventSecurityUser transientInstance) {
		log.debug("persisting EventSecurityUser instance");
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
	 * com.coombu.photobook.dao.IEventSecurityUserDao#delete(com.coombu.photobook
	 * .model.EventSecurityUser)
	 */
	@Override
	public void delete(EventSecurityUser persistentInstance) {
		log.debug("removing EventSecurityUser instance");
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
	 * com.coombu.photobook.dao.IEventSecurityUserDao#merge(com.coombu.photobook
	 * .model.EventSecurityUser)
	 */
	@Override
	public EventSecurityUser merge(EventSecurityUser detachedInstance) {
		log.debug("merging EventSecurityUser instance");
		try {
			EventSecurityUser result = (EventSecurityUser) super
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
	 * @see com.coombu.photobook.dao.IEventSecurityUserDao#findById(int)
	 */
	public EventSecurityUser findById(int id) {
		log.debug("getting EventSecurityUser instance with id: " + id);
		try {
			EventSecurityUser instance = (EventSecurityUser) super.findById(
					EventSecurityUser.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventSecurityUser> getEventSecurityUser(long securityUserId) {
		return (List<EventSecurityUser>) getCurrentSession()
				.createCriteria(EventSecurityUser.class)
				.add(Restrictions.eq("securityUser.securityUserId",
						securityUserId))
				.setFetchMode("event.schedules", FetchMode.EAGER).list();
	}

	@Override
	public EventSecurityUser getEventSecurityUser(long securityUserId,
			long eventId) {
		return (EventSecurityUser) getCurrentSession()
				.createCriteria(EventSecurityUser.class)
				.add(Restrictions.eq("securityUser.securityUserId",
						securityUserId))
				.add(Restrictions.eq("event.eventId", eventId)).uniqueResult();
	}

	@Override
	public SecurityUser getEventSecurityUserById(long eventSecUserId) {
		// TODO Auto-generated method stub
		return ((EventSecurityUser) getCurrentSession()
				.createCriteria(EventSecurityUser.class)
				.add(Restrictions.eq("eventSecurityUserId", eventSecUserId))
				.setFetchMode("securityUser", FetchMode.JOIN).uniqueResult())
				.getSecurityUser();
	}

	@Override
	public boolean checkUserExistenceBasedOnRole(long eventId, long roleId,
			long eventSecUserId) {
		// TODO Auto-generated method stub
		Criteria cri = getCurrentSession().createCriteria(
				EventSecurityUser.class);
		cri.add(Restrictions.eq("event.eventId", eventId));
		cri.add(Restrictions.ne("securityUser.securityUserId", eventSecUserId));
		List<EventSecurityUser> user = ((List<EventSecurityUser>) cri.add(
				Restrictions.eq("roleId", (short) roleId)).list());

		if (user != null && user.size() > 0) {
			log.debug("Student Rep is already exists!");

			for (EventSecurityUser us : user) {
				log.debug("User name:" + us.getSecurityUser().getUserName());
			}
			return true;
		}
		log.debug("Student Rep is not exists!");
		return false;
	}

	@Override
	public synchronized boolean incrementImageVoteCount(long eventSecUserId) {

		Criteria cri = getCurrentSession().createCriteria(
				EventSecurityUser.class);
		EventSecurityUser user = (EventSecurityUser) cri.add(Restrictions.eq(
				"eventSecurityUserId", eventSecUserId));
		log.debug("event security user image vote count increment is called!");
		if (user != null) {

			user.setVoteCount(user.getVoteCount() + 1);
			super.update(user);
			log.debug("event security user vote count is incremented!");
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean decrementImageVoteCount(long eventSecUserId) {
		Criteria cri = getCurrentSession().createCriteria(
				EventSecurityUser.class);
		EventSecurityUser user = (EventSecurityUser) cri.add(Restrictions.eq(
				"eventSecurityUserId", eventSecUserId));
		log.debug("event security user image vote count decrement is called!");
		if (user != null) {

			user.setVoteCount(user.getVoteCount() - 1);
			super.update(user);
			log.debug("event security user vote count is decremented!");
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventSecurityUser> getEventSecurityUsersByEventId(long eventId,
			boolean wantActive, boolean wantInActive, boolean wantDeleted,
			boolean wantPending, boolean wantBlocked) {
		log.debug("current event id is:" + eventId);

		Criteria cri = getCurrentSession().createCriteria(
				EventSecurityUser.class);
		Disjunction dis = Restrictions.disjunction();

		if (wantActive) {
			dis.add(Restrictions.eq("userStatusTypeId",
					Long.parseLong(USER_STATUS_TYPE.ACTIVE.id() + "")));
			log.debug("user is criteria is included the active status");
		}

		if (wantInActive) {
			dis.add(Restrictions.eq("userStatusTypeId",
					Long.parseLong(USER_STATUS_TYPE.INACTIVE.id() + "")));
			log.debug("user is criteria is included the inactive status");
		}

		if (wantDeleted) {
			dis.add(Restrictions.eq("userStatusTypeId",
					Long.parseLong(USER_STATUS_TYPE.DELETED.id() + "")));
			log.debug("user is criteria is included the Deleted status");
		}

		if (wantPending) {
			dis.add(Restrictions.eq("userStatusTypeId",
					Long.parseLong(USER_STATUS_TYPE.PENDING.id() + "")));
			log.debug("user is criteria is included the pending status");
		}

		if (wantBlocked) {
			dis.add(Restrictions.eq("userStatusTypeId",
					Long.parseLong(USER_STATUS_TYPE.BLOCKED.id() + "")));
			log.debug("user is criteria is included the blocked status");
		}

		if (dis != null) {
			cri.add(dis);
		}

		return (List<EventSecurityUser>) cri.add(
				Restrictions.eq("event.eventId", eventId)).list();
	}

	@Override
	public Map<String, Object> validateResendEmail(long securityUserId,
			long currentEventId) {

		Map<String, Object> options = new HashMap<String, Object>();

		options.put("existsAlready", false);

		options.put("existingGroups", null);

		List<String> eventWithStatus = null;

		Criteria cri = getCurrentSession().createCriteria(
				EventSecurityUser.class);
		cri.add(Restrictions.ne("event.eventId", currentEventId));

		List<EventSecurityUser> usersList = cri.add(
				Restrictions.eq("securityUser.securityUserId", securityUserId))
				.list();

		if (usersList != null && !usersList.isEmpty()) {
			log.debug("user exists in different group! and the total groups count is :"
					+ usersList.size());
			options.put("existsAlready", true);

			eventWithStatus = new ArrayList<String>(usersList.size());

			for (EventSecurityUser user : usersList) {
				log.debug("user already exists in this "
						+ user.getEvent().getEventName()
						+ " group with following status :" + user.getRoleId());
				eventWithStatus.add(user.getEvent().getEventName() + "~"
						+ user.getUserStatusTypeId());
			}

			options.put("existingGroups", eventWithStatus);
		}

		return options;
	}
}
