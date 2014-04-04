package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.Privilege;

/**
 * Home object for domain model class Privilege.
 * @see com.coombu.photobook.model.lookup.Privilege
 * @author Fekade Aytaged
 */

public class PrivilegeDao extends HibernateBaseDao implements IPrivilege   {

	private static final Logger log = LoggerFactory
			.getLogger(PrivilegeDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IPrivilege#save(com.coombu.photobook.model.lookup.Privilege)
	 */
	@Override
	public void save(Privilege transientInstance) {
		log.debug("persisting Privilege instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IPrivilege#delete(com.coombu.photobook.model.lookup.Privilege)
	 */
	@Override
	public void delete(Privilege persistentInstance) {
		log.debug("removing Privilege instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IPrivilege#merge(com.coombu.photobook.model.lookup.Privilege)
	 */
	@Override
	public Privilege merge(Privilege detachedInstance) {
		log.debug("merging Privilege instance");
		try {
			Privilege result = (Privilege)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IPrivilege#findById(short)
	 */
	public Privilege findById(short id) {
		log.debug("getting Privilege instance with id: " + id);
		try {
			Privilege instance = (Privilege) super
					.findById(Privilege.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
