package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.ImageStatusType;

/**
 * Home object for domain model class ImageStatusType.
 * @see com.coombu.photobook.model.lookup.ImageStatusType
 * @author Fekade Aytaged
 */

public class ImageStatusTypeDao extends HibernateBaseDao implements IImageStatusType 
		 {

	private static final Logger log = LoggerFactory
			.getLogger(ImageStatusTypeDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IImageStatusType#save(com.coombu.photobook.model.lookup.ImageStatusType)
	 */
	@Override
	public void save(ImageStatusType transientInstance) {
		log.debug("persisting ImageStatusType instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IImageStatusType#delete(com.coombu.photobook.model.lookup.ImageStatusType)
	 */
	@Override
	public void delete(ImageStatusType persistentInstance) {
		log.debug("removing ImageStatusType instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IImageStatusType#merge(com.coombu.photobook.model.lookup.ImageStatusType)
	 */
	@Override
	public ImageStatusType merge(ImageStatusType detachedInstance) {
		log.debug("merging ImageStatusType instance");
		try {
			ImageStatusType result = (ImageStatusType)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IImageStatusType#findById(short)
	 */
	public ImageStatusType findById(short id) {
		log.debug("getting ImageStatusType instance with id: " + id);
		try {
			ImageStatusType instance = (ImageStatusType) super.findById(
					ImageStatusType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
