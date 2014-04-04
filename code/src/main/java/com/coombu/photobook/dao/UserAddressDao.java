package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.model.UserAddress;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class UserAddress.
 * @see com.coombu.photobook.model.UserAddress
 * @author Fekade Aytaged
 */

public class UserAddressDao extends HibernateBaseDao implements IUserAddressDao 
{

    private static final Logger log = LoggerFactory.getLogger(UserAddressDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IUserAddressDao#save(com.coombu.photobook.model.UserAddress)
	 */
    @Override
    public void save(UserAddress transientInstance) {
        log.debug("persisting UserAddress instance");
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
	 * @see com.coombu.photobook.dao.IUserAddressDao#delete(com.coombu.photobook.model.UserAddress)
	 */
    @Override
    public void delete(UserAddress persistentInstance) {
        log.debug("removing UserAddress instance");
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
	 * @see com.coombu.photobook.dao.IUserAddressDao#merge(com.coombu.photobook.model.UserAddress)
	 */
    @Override
    public UserAddress merge(UserAddress detachedInstance) {
        log.debug("merging UserAddress instance");
        try {
            UserAddress result = (UserAddress)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IUserAddressDao#findById(int)
	 */
    public UserAddress findById( int id) {
        log.debug("getting UserAddress instance with id: " + id);
        try {
            UserAddress instance = (UserAddress) super.findById(UserAddress.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

