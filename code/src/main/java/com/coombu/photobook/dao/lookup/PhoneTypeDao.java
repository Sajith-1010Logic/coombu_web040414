package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.PhoneType;

/**
 * Home object for domain model class PhoneType.
 * @see com.coombu.photobook.model.lookup.PhoneType
 * @author Fekade Aytaged
 */

public class PhoneTypeDao extends HibernateBaseDao implements IPhoneType   {

	private static final Logger log = LoggerFactory
			.getLogger(PhoneTypeDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IPhoneType#save(com.coombu.photobook.model.lookup.PhoneType)
	 */
	@Override
	public void save(PhoneType transientInstance) {
		log.debug("persisting PhoneType instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IPhoneType#delete(com.coombu.photobook.model.lookup.PhoneType)
	 */
	@Override
	public void delete(PhoneType persistentInstance) {
		log.debug("removing PhoneType instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IPhoneType#merge(com.coombu.photobook.model.lookup.PhoneType)
	 */
	@Override
	public PhoneType merge(PhoneType detachedInstance) {
		log.debug("merging PhoneType instance");
		try {
			PhoneType result = (PhoneType)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IPhoneType#findById(short)
	 */
	public PhoneType findById(short id) {
		log.debug("getting PhoneType instance with id: " + id);
		try {
			PhoneType instance = (PhoneType) super
					.findById(PhoneType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
