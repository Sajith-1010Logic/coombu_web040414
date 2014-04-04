package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.CommentStatusType;

/**
 * Home object for domain model class CommentStatusType.
 * @see com.coombu.photobook.model.lookup.CommentStatusType
 * @author Fekade Aytaged
 */

public class CommentStatusTypeDao extends HibernateBaseDao implements ICommentStatusType 
		{

	private static final Logger log = LoggerFactory
			.getLogger(CommentStatusTypeDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.ICommentStatusType#save(com.coombu.photobook.model.lookup.CommentStatusType)
	 */
	@Override
	public void save(CommentStatusType transientInstance) {
		log.debug("persisting CommentStatusType instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.ICommentStatusType#delete(com.coombu.photobook.model.lookup.CommentStatusType)
	 */
	@Override
	public void delete(CommentStatusType persistentInstance) {
		log.debug("removing CommentStatusType instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.ICommentStatusType#merge(com.coombu.photobook.model.lookup.CommentStatusType)
	 */
	@Override
	public CommentStatusType merge(CommentStatusType detachedInstance) {
		log.debug("merging CommentStatusType instance");
		try {
			CommentStatusType result = (CommentStatusType)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.ICommentStatusType#findById(short)
	 */
	public CommentStatusType findById(short id) {
		log.debug("getting CommentStatusType instance with id: " + id);
		try {
			CommentStatusType instance = (CommentStatusType) super.findById(
					CommentStatusType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
