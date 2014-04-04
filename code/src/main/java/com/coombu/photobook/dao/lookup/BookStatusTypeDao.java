package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.BookStatusType;

/**
 * Home object for domain model class BookStatusType.
 * @see com.coombu.photobook.model.lookup.BookStatusType
 * @author Fekade Aytaged
 */

public class BookStatusTypeDao extends HibernateBaseDao implements
		IBookStatusType {

	private static final Logger log = LoggerFactory
			.getLogger(BookStatusTypeDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IBookStatusType#save(com.coombu.photobook.model.lookup.BookStatusType)
	 */
	@Override
	public void save(BookStatusType transientInstance) {
		log.debug("persisting BookStatusType instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IBookStatusType#delete(com.coombu.photobook.model.lookup.BookStatusType)
	 */
	@Override
	public void delete(BookStatusType persistentInstance) {
		log.debug("removing BookStatusType instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IBookStatusType#merge(com.coombu.photobook.model.lookup.BookStatusType)
	 */
	@Override
	public BookStatusType merge(BookStatusType detachedInstance) {
		log.debug("merging BookStatusType instance");
		try {
			BookStatusType result = (BookStatusType)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IBookStatusType#findById(byte)
	 */
	public BookStatusType findById(byte id) {
		log.debug("getting BookStatusType instance with id: " + id);
		try {
			BookStatusType instance = (BookStatusType) super.findById(
					BookStatusType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
