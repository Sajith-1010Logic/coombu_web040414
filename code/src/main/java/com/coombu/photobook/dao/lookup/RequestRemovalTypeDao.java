package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.RequestRemovalType;

/**
 * Home object for domain model class RequestRemovalType.
 * @see com.coombu.photobook.model.lookup.RequestRemovalType
 * @author Fekade Aytaged
 */

public class RequestRemovalTypeDao extends HibernateBaseDao implements IRequestRemovalType 
		 {

	private static final Logger log = LoggerFactory
			.getLogger(RequestRemovalTypeDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRequestRemovalType#save(com.coombu.photobook.model.lookup.RequestRemovalType)
	 */
	@Override
	public void save(RequestRemovalType transientInstance) {
		log.debug("persisting RequestRemovalType instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRequestRemovalType#delete(com.coombu.photobook.model.lookup.RequestRemovalType)
	 */
	@Override
	public void delete(RequestRemovalType persistentInstance) {
		log.debug("removing RequestRemovalType instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRequestRemovalType#merge(com.coombu.photobook.model.lookup.RequestRemovalType)
	 */
	@Override
	public RequestRemovalType merge(RequestRemovalType detachedInstance) {
		log.debug("merging RequestRemovalType instance");
		try {
			RequestRemovalType result = (RequestRemovalType)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRequestRemovalType#findById(int)
	 */
	public RequestRemovalType findById(int id) {
		log.debug("getting RequestRemovalType instance with id: " + id);
		try {
			RequestRemovalType instance = (RequestRemovalType) super.findById(
					RequestRemovalType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
