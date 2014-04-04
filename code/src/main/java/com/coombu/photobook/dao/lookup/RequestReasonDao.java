package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.RequestReason;

/**
 * Home object for domain model class RequestReason.
 * @see com.coombu.photobook.model.lookup.RequestReason
 * @author Fekade Aytaged
 */

public class RequestReasonDao extends HibernateBaseDao implements IRequestReason 
		 {

	private static final Logger log = LoggerFactory
			.getLogger(RequestReasonDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRequestReason#save(com.coombu.photobook.model.lookup.RequestReason)
	 */
	@Override
	public void save(RequestReason transientInstance) {
		log.debug("persisting RequestReason instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRequestReason#delete(com.coombu.photobook.model.lookup.RequestReason)
	 */
	@Override
	public void delete(RequestReason persistentInstance) {
		log.debug("removing RequestReason instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRequestReason#merge(com.coombu.photobook.model.lookup.RequestReason)
	 */
	@Override
	public RequestReason merge(RequestReason detachedInstance) {
		log.debug("merging RequestReason instance");
		try {
			RequestReason result = (RequestReason)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRequestReason#findById(int)
	 */
	public RequestReason findById(int id) {
		log.debug("getting RequestReason instance with id: " + id);
		try {
			RequestReason instance = (RequestReason) super.findById(
					RequestReason.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
