package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.ResolutionStatus;

/**
 * Home object for domain model class ResolutionStatus.
 * @see com.coombu.photobook.model.lookup.ResolutionStatus
 * @author Fekade Aytaged
 */

public class ResolutionStatusDao extends HibernateBaseDao implements IResolutionStatus 
		 {

	private static final Logger log = LoggerFactory
			.getLogger(ResolutionStatusDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IResolutionStatus#save(com.coombu.photobook.model.lookup.ResolutionStatus)
	 */
	@Override
	public void save(ResolutionStatus transientInstance) {
		log.debug("persisting ResolutionStatus instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IResolutionStatus#delete(com.coombu.photobook.model.lookup.ResolutionStatus)
	 */
	@Override
	public void delete(ResolutionStatus persistentInstance) {
		log.debug("removing ResolutionStatus instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IResolutionStatus#merge(com.coombu.photobook.model.lookup.ResolutionStatus)
	 */
	@Override
	public ResolutionStatus merge(ResolutionStatus detachedInstance) {
		log.debug("merging ResolutionStatus instance");
		try {
			ResolutionStatus result = (ResolutionStatus)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IResolutionStatus#findById(short)
	 */
	public ResolutionStatus findById(short id) {
		log.debug("getting ResolutionStatus instance with id: " + id);
		try {
			ResolutionStatus instance = (ResolutionStatus) super.findById(
					ResolutionStatus.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
