package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.ImageSource;

/**
 * Home object for domain model class ImageSource.
 * @see com.coombu.photobook.model.lookup.ImageSource
 * @author Fekade Aytaged
 */

public class ImageSourceDao extends HibernateBaseDao implements IImageSource   {

	private static final Logger log = LoggerFactory
			.getLogger(ImageSourceDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IImageSource#save(com.coombu.photobook.model.lookup.ImageSource)
	 */
	@Override
	public void save(ImageSource transientInstance) {
		log.debug("persisting ImageSource instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IImageSource#delete(com.coombu.photobook.model.lookup.ImageSource)
	 */
	@Override
	public void delete(ImageSource persistentInstance) {
		log.debug("removing ImageSource instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IImageSource#merge(com.coombu.photobook.model.lookup.ImageSource)
	 */
	@Override
	public ImageSource merge(ImageSource detachedInstance) {
		log.debug("merging ImageSource instance");
		try {
			ImageSource result = (ImageSource)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IImageSource#findById(short)
	 */
	public ImageSource findById(short id) {
		log.debug("getting ImageSource instance with id: " + id);
		try {
			ImageSource instance = (ImageSource) super.findById(
					ImageSource.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
