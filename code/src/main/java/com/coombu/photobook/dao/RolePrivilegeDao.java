package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.model.RolePrivilege;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class RolePrivilege.
 * @see com.coombu.photobook.model.RolePrivilege
 * @author Fekade Aytaged
 */

public class RolePrivilegeDao extends HibernateBaseDao implements IRolePrivilegeDao 
{

    private static final Logger log = LoggerFactory.getLogger(RolePrivilegeDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IRolePrivilegeDao#save(com.coombu.photobook.model.RolePrivilege)
	 */
    @Override
    public void save(RolePrivilege transientInstance) {
        log.debug("persisting RolePrivilege instance");
        try {
            super.save(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IRolePrivilegeDao#delete(com.coombu.photobook.model.RolePrivilege)
	 */
    @Override
    public void delete(RolePrivilege persistentInstance) {
        log.debug("removing RolePrivilege instance");
        try {
            super.delete(persistentInstance);
            log.debug("remove successful");
        }
        catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IRolePrivilegeDao#merge(com.coombu.photobook.model.RolePrivilege)
	 */
    @Override
    public RolePrivilege merge(RolePrivilege detachedInstance) {
        log.debug("merging RolePrivilege instance");
        try {
            RolePrivilege result = (RolePrivilege)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IRolePrivilegeDao#findById(int)
	 */
    public RolePrivilege findById( int id) {
        log.debug("getting RolePrivilege instance with id: " + id);
        try {
            RolePrivilege instance = (RolePrivilege) super.findById(RolePrivilege.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

