package com.coombu.photobook.dao.lookup;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.Role;

/**
 * Home object for domain model class Role.
 * @see com.coombu.photobook.model.lookup.Role
 * @author Fekade Aytaged
 */

public class RoleDao extends HibernateBaseDao implements IRoleDao   {

	private static final Logger log = LoggerFactory.getLogger(RoleDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRole#save(com.coombu.photobook.model.lookup.Role)
	 */
	@Override
	public void save(Role transientInstance) {
		log.debug("persisting Role instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRole#delete(com.coombu.photobook.model.lookup.Role)
	 */
	@Override
	public void delete(Role persistentInstance) {
		log.debug("removing Role instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRole#merge(com.coombu.photobook.model.lookup.Role)
	 */
	@Override
	public Role merge(Role detachedInstance) {
		log.debug("merging Role instance");
		try {
			Role result = (Role)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IRole#findById(short)
	 */
	public Role findById(short id) {
		log.debug("getting Role instance with id: " + id);
		try {
			Role instance = (Role) super.findById(Role.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Role> getAll()
	{
		return (List<Role>)getCurrentSession().createQuery("FROM Role").list();
	}
}
