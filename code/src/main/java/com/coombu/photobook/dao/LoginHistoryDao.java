package com.coombu.photobook.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.LoginHistory;
import com.coombu.photobook.model.SecurityUser;

// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class LoginHistory.
 * 
 * @see com.coombu.photobook.model.LoginHistory
 * @author Fekade Aytaged
 */

@Repository
public class LoginHistoryDao extends HibernateBaseDao implements
		ILoginHistoryDao {

	private static final Logger log = LoggerFactory
			.getLogger(LoginHistoryDao.class);

	@Autowired
	EventSecurityUserDao eventSecurityUserDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.ILoginHistoryDao#save(com.coombu.photobook.model
	 * .LoginHistory)
	 */
	@Override
	public void save(LoginHistory transientInstance) {
		log.debug("persisting LoginHistory instance");
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
	 * com.coombu.photobook.dao.ILoginHistoryDao#delete(com.coombu.photobook
	 * .model.LoginHistory)
	 */
	@Override
	public void delete(LoginHistory persistentInstance) {
		log.debug("removing LoginHistory instance");
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
	 * com.coombu.photobook.dao.ILoginHistoryDao#merge(com.coombu.photobook.
	 * model.LoginHistory)
	 */
	@Override
	public LoginHistory merge(LoginHistory detachedInstance) {
		log.debug("merging LoginHistory instance");
		try {
			LoginHistory result = (LoginHistory) super.merge(detachedInstance);
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
	 * @see com.coombu.photobook.dao.ILoginHistoryDao#findById(int)
	 */
	@Override
	public LoginHistory findById(int id) {
		log.debug("getting LoginHistory instance with id: " + id);
		try {
			LoginHistory instance = (LoginHistory) super.findById(
					LoginHistory.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public void saveOrUpdate(LoginHistory history) {
		log.debug("persisting LoginHistory instance");
		try {
			super.saveOrUpdate(history);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventSecurityUser> getLoggedInUsers(long eventId) {
		List<EventSecurityUser> users = null;
		try {

			Calendar cal = GregorianCalendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -5);
			Timestamp now = new Timestamp(cal.getTimeInMillis());

			DetachedCriteria logins = DetachedCriteria
					.forClass(LoginHistory.class, "l")
					.setProjection(Projections.max("l.loginTimestamp"))
					.setProjection(
							Projections
									.groupProperty("securityUser.securityUserId"))
					.add(Property.forName("l.logoutTimestamp").isNull())
					.addOrder(Order.desc("l.loginTimestamp"))
					.add(Restrictions.gt("l.loginTimestamp", now));
			Criteria criteria = getSessionFactory().getCurrentSession()
					.createCriteria(EventSecurityUser.class, "a");
			criteria.add(Restrictions.eq("a.event.eventId", eventId));
			criteria.add(Subqueries.propertyIn("a.securityUser.securityUserId",
					logins));
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			users = criteria.list();
		} catch (Exception e) {
			log.error("Error: ", e);
			throw e;
		}
		if (users == null)
			users = new ArrayList<EventSecurityUser>();
		return users;
	}

	@Override
	public List<LoginHistory> getAllHistoryFromOrNowAndToDate(
			long currentEventId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub

		log.debug("fetching login history list for the current event :"
				+ currentEventId);

		Criteria cri = null;
		List<LoginHistory> historyList = null;

		cri = getCurrentSession().createCriteria(SecurityUser.class, "su")
				.createAlias("su.eventSecurityUsers", "evs");

		cri.setProjection(Projections.property("su.securityUserId"));

		List<EventSecurityUser> usersList = eventSecurityUserDao
				.getEventSecurityUsersByEventId(currentEventId, false, false, false, false, false);

		if (usersList != null)
			log.debug("List of eventSecurityUsers :" + usersList.size());

		List<Long> securityIds = null;

		cri = (Criteria) getCurrentSession().createCriteria(LoginHistory.class);

		if (usersList != null && !usersList.isEmpty()) {
			securityIds = new ArrayList<Long>();

			for (EventSecurityUser user : usersList) {
				securityIds.add(user.getSecurityUser().getSecurityUserId());
			}

			cri.add(Restrictions.in("securityUserId", securityIds));
			log.debug("users found for the login and logout history report. total users is:"
					+ usersList.size());
		}
		if (startDate != null)
			cri.add(Restrictions.ge("loginTimestamp", startDate));

		if (endDate != null)
			cri.add(Restrictions.le("loginTimestamp", endDate));

		cri.setFetchMode("securityUser", FetchMode.JOIN);

		cri.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		historyList = (List<LoginHistory>) cri.list();

		log.debug("totak historys is: " + historyList.size());

		return historyList;
	}
}
