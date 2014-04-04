package com.coombu.photobook.dao.lookup;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.UserStatus;

/**
 * Home object for domain model class UserStatus.
 * @see com.coombu.photobook.model.lookup.UserStatus
 * @author Fekade Aytaged
 */

public class UserStatusDao extends HibernateBaseDao implements IUserStatusDao   {

	private static final Logger log = LoggerFactory
			.getLogger(UserStatusDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IUserStatus#save(com.coombu.photobook.model.lookup.UserStatus)
	 */
	@Override
	public void save(UserStatus transientInstance) {
		log.debug("persisting UserStatus instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IUserStatus#delete(com.coombu.photobook.model.lookup.UserStatus)
	 */
	@Override
	public void delete(UserStatus persistentInstance) {
		log.debug("removing UserStatus instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IUserStatus#merge(com.coombu.photobook.model.lookup.UserStatus)
	 */
	@Override
	public UserStatus merge(UserStatus detachedInstance) {
		log.debug("merging UserStatus instance");
		try {
			UserStatus result = (UserStatus)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IUserStatus#findById(short)
	 */
	public UserStatus findById(short id) {
		log.debug("getting UserStatus instance with id: " + id);
		try {
			UserStatus instance = (UserStatus) super.findById(UserStatus.class,
					id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<UserStatus> getAll()
	{
		return (List<UserStatus>)getCurrentSession().createQuery("FROM UserStatus").list();
	}
}
