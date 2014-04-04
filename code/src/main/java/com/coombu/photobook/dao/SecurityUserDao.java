package com.coombu.photobook.dao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.model.UserProfile;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.presentation.search.UserSearchCriteria;
import com.coombu.photobook.util.Constants.USER_STATUS_TYPE;

/**
 * Home object for domain model class SecurityUser.
 * 
 * @see com.coombu.photobook.model.SecurityUser
 * @author Fekade Aytaged
 */
@Repository
public class SecurityUserDao extends HibernateBaseDao implements
		ISecurityUserDao {

	private static final Logger log = LoggerFactory
			.getLogger(SecurityUserDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.ISecurityUserDao#save(com.coombu.photobook.model
	 * .SecurityUser)
	 */
	@Override
	public void save(SecurityUser transientInstance) {
		log.debug("persisting SecurityUser instance");
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
	 * com.coombu.photobook.dao.ISecurityUserDao#delete(com.coombu.photobook
	 * .model.SecurityUser)
	 */
	@Override
	public void delete(SecurityUser persistentInstance) {
		log.debug("removing SecurityUser instance");
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
	 * com.coombu.photobook.dao.ISecurityUserDao#merge(com.coombu.photobook.
	 * model.SecurityUser)
	 */
	@Override
	public SecurityUser merge(SecurityUser detachedInstance) {
		log.debug("merging SecurityUser instance");
		try {
			SecurityUser result = (SecurityUser) super.merge(detachedInstance);
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
	 * @see com.coombu.photobook.dao.ISecurityUserDao#findById(int)
	 */
	public SecurityUser findById(int id) {
		log.debug("getting SecurityUser instance with id: " + id);
		try {
			SecurityUser instance = (SecurityUser) super.findById(
					SecurityUser.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.ISecurityUserDao#findUser(com.coombu.photobook
	 * .presentation.search.UserSearchCriteria, int, java.lang.String,
	 * org.hibernate.criterion.Order)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SecurityUser> findUser(UserSearchCriteria searchCriteria,
			int firstResult, String sortField, Order ascDesc) {
		Criteria criteria = getCurrentSession().createCriteria(
				SecurityUser.class);
		setCriteria(criteria, searchCriteria);
		return (List<SecurityUser>) criteria.addOrder(ascDesc)
				.setMaxResults(searchCriteria.getNumRowsReturned())
				.setFirstResult(firstResult).list();

	}

	private void setCriteria(Criteria criteria, UserSearchCriteria usc) {
		criteria.add(Restrictions.ne(
				"userStatusTypeId", USER_STATUS_TYPE.DELETED.id())); //$NON-NLS-1$
		if (!isEmpty(usc.getUserType()))
			criteria.add(Restrictions.eq("userType", //$NON-NLS-1$
					usc.getUserType()));
		if (!isEmpty(usc.getLastName()) || !isEmpty(usc.getFirstName())
				|| !isEmpty(usc.getZip()) || !isEmpty(usc.getEmailAddress())) {
			Disjunction or = Restrictions.disjunction();
			criteria.add(Restrictions.disjunction());
			if (!isEmpty(usc.getLastName())) {
				or.add(Restrictions
						.like("lastName", usc.getLastName(), MatchMode.ANYWHERE).ignoreCase()); //$NON-NLS-1$
			}
			if (!isEmpty(usc.getFirstName())) {
				or.add(Restrictions
						.like("firstName", usc.getFirstName(), MatchMode.ANYWHERE).ignoreCase()); //$NON-NLS-1$
			}
			if (!isEmpty(usc.getEmailAddress())) {
				or.add(Restrictions
						.like("userId", usc.getEmailAddress(), MatchMode.ANYWHERE).ignoreCase()); //$NON-NLS-1$
			}
			criteria.add(or);
		}
	}

	private boolean isEmpty(String str) {
		if (str != null && str.trim().isEmpty() == false)
			return false;
		else
			return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.ISecurityUserDao#getNumberOfUsers(com.coombu
	 * .photobook.presentation.search.UserSearchCriteria)
	 */
	@Override
	public int getNumberOfUsers(UserSearchCriteria searchCriteria) {
		Criteria criteria = getCurrentSession().createCriteria(
				SecurityUser.class);
		this.setCriteria(criteria, searchCriteria);
		Long countList = (Long) criteria.setProjection(Projections.rowCount())
				.list().get(0);
		return countList.intValue();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coombu.photobook.dao.ISecurityUserDao#findUser(java.lang.String)
	 */
	@Override
	public SecurityUser findUser(String userId) {

		SecurityUser user = null;
		user = (SecurityUser) getCurrentSession()
				.createCriteria(SecurityUser.class)
				.add(Restrictions.eq("userName", userId).ignoreCase()) //$NON-NLS-1$
				.uniqueResult();
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.ISecurityUserDao#getLastLoggedInTimestamp(java
	 * .lang.Long)
	 */
	@Override
	public Timestamp getLastLoggedInTimestamp(Long securityUserId) {
		Timestamp ts = null;
		String sql = "select max(LOGIN_TMPSTMP) from login_history where security_user_id = :securityUserId";
		ts = (Timestamp) getCurrentSession().createSQLQuery(sql)
				.setParameter("securityUserId", securityUserId) //$NON-NLS-1$
				.uniqueResult();
		return ts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.ISecurityUserDao#resetPassword(com.coombu.photobook
	 * .model.SecurityUser, java.lang.String)
	 */
	@Override
	public boolean resetPassword(SecurityUser user, String passwordHash) {
		String sql = "UPDATE SECURITY_USER SET PASSWORD_HASH = :password WHERE USERID = :userId"; //$NON-NLS-1$

		int update = getCurrentSession().createSQLQuery(sql)
				.setParameter("password", passwordHash) //$NON-NLS-1$
				.setParameter("userId", user.getUserName()) //$NON-NLS-1$
				.executeUpdate();
		log.info(
				"Updated security password {}  rows in SECURITY_USER for : {}", update, user.getUserName()); //$NON-NLS-1$

		return true;
	}

	@Override
	public void update(SecurityUser dbUser) {
		getCurrentSession().update(dbUser);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecurityUser> getUsers() {
		List<SecurityUser> user = (List<SecurityUser>) getCurrentSession()
				.createCriteria(SecurityUser.class)
				.add(Restrictions.eq("userStatusId",
						(short) USER_STATUS_TYPE.ACTIVE.id())).list();

		return user;

	}

	@Override
	public Map<String, Object> checkUserExistenceByEmailId(long eventId,
			String email, String userName) {
		log.debug("Entered  checkUserExistenceByEmailId");
		Map<String, Object> options = new HashMap<String, Object>();

		options.put("email", true);
		options.put("userName", true);
		options.put("status", 0);

		Criteria criteria = getCurrentSession().createCriteria(
				UserProfile.class);

		criteria.add(Restrictions.eq("emailAddress", email));

		List<Object[]> list = criteria.list();

		if (list != null && list.size() > 0) {
			log.debug("Users found for given email:" + list.size());
			options.put("email", false);
		}

		criteria = getCurrentSession().createCriteria(SecurityUser.class);
		criteria.add(Restrictions.eq("userName", userName));

		SecurityUser user = (SecurityUser) criteria.uniqueResult();

		EventSecurityUser eventSecurityUser = null;

		if (user != null) {
			
			log.debug("Users found for given userName: {}", user.getUserName());
			options.put("userName", false);

			criteria = getCurrentSession().createCriteria(
					EventSecurityUser.class);
			criteria.add(Restrictions.eq("event.eventId", eventId));
			criteria.add(Restrictions.eq("securityUser.securityUserId",
					user.getSecurityUserId()));

			eventSecurityUser = (EventSecurityUser) criteria.uniqueResult();
		}

		if (eventSecurityUser != null) {
			log.debug("Event security user found!"
					+ eventSecurityUser.getEventSecurityUserId());
			options.put("status", eventSecurityUser.getUserStatusTypeId());
		} else {
			log.debug("Event security user is not found!");
		}

		log.debug("Exited  checkUserExistenceByEmailId");
		return options;
	}

	@Override
	public SecurityUser getSecurityUser(long eventId, String email,
			String userName) {
		// TODO Auto-generated method stub
		SecurityUser user = findUser(userName);

		if (user != null) {

			List<String> result = null;

			Criteria criteria = getCurrentSession().createCriteria(
					SecurityUser.class, "su").createAlias("eventSecurityUsers",
					"evsu");

			criteria.add(Restrictions.eq("evsu.securityUser.securityUserId",
					user.getSecurityUserId()));
			criteria.add(Restrictions.eq("evsu.event.eventId", eventId));

			result = (List<String>) criteria.setProjection(
					Projections.property("su.userName")).list();

			if (result != null && result.size() > 0 && result.get(0) != null) {
				log.debug("User is already exists in this group with following details:"
						+ result.get(0));
				return null;
			}
			log.debug("user is not exists in this group!");
			return user;
		} else {
			log.debug("user is not exists in this group!");
			return user;
		}

	}

}
