package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.model.SecurityUserRole;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class SecurityUserRole.
 * @see com.coombu.photobook.model.SecurityUserRole
 * @author Fekade Aytaged
 */

public class SecurityUserRoleDao extends HibernateBaseDao implements ISecurityUserRoleDao 
{

    private static final Logger log = LoggerFactory.getLogger(SecurityUserRoleDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.ISecurityUserRoleDao#save(com.coombu.photobook.model.SecurityUserRole)
	 */
    @Override
    public void save(SecurityUserRole transientInstance) {
        log.debug("persisting SecurityUserRole instance");
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
	 * @see com.coombu.photobook.dao.ISecurityUserRoleDao#delete(com.coombu.photobook.model.SecurityUserRole)
	 */
    @Override
    public void delete(SecurityUserRole persistentInstance) {
        log.debug("removing SecurityUserRole instance");
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
	 * @see com.coombu.photobook.dao.ISecurityUserRoleDao#merge(com.coombu.photobook.model.SecurityUserRole)
	 */
    @Override
    public SecurityUserRole merge(SecurityUserRole detachedInstance) {
        log.debug("merging SecurityUserRole instance");
        try {
            SecurityUserRole result = (SecurityUserRole)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.ISecurityUserRoleDao#findById(int)
	 */
    public SecurityUserRole findById( int id) {
        log.debug("getting SecurityUserRole instance with id: " + id);
        try {
            SecurityUserRole instance = (SecurityUserRole) super.findById(SecurityUserRole.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

