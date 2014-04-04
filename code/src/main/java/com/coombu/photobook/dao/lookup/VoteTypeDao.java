package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.VoteType;

/**
 * Home object for domain model class VoteType.
 * @see com.coombu.photobook.model.lookup.VoteType
 * @author Fekade Aytaged
 */

public class VoteTypeDao extends HibernateBaseDao implements IVoteType   {

	private static final Logger log = LoggerFactory
			.getLogger(VoteTypeDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IVoteType#save(com.coombu.photobook.model.lookup.VoteType)
	 */
	@Override
	public void save(VoteType transientInstance) {
		log.debug("persisting VoteType instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IVoteType#delete(com.coombu.photobook.model.lookup.VoteType)
	 */
	@Override
	public void delete(VoteType persistentInstance) {
		log.debug("removing VoteType instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IVoteType#merge(com.coombu.photobook.model.lookup.VoteType)
	 */
	@Override
	public VoteType merge(VoteType detachedInstance) {
		log.debug("merging VoteType instance");
		try {
			VoteType result = (VoteType)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IVoteType#findById(int)
	 */
	public VoteType findById(int id) {
		log.debug("getting VoteType instance with id: " + id);
		try {
			VoteType instance = (VoteType) super.findById(VoteType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
