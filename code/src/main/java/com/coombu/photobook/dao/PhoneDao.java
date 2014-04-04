package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.model.Phone;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class Phone.
 * @see com.coombu.photobook.model.Phone
 * @author Fekade Aytaged
 */

public class PhoneDao extends HibernateBaseDao implements IPhoneDao 
{

    private static final Logger log = LoggerFactory.getLogger(PhoneDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IPhoneDao#save(com.coombu.photobook.model.Phone)
	 */
    @Override
    public void save(Phone transientInstance) {
        log.debug("persisting Phone instance");
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
	 * @see com.coombu.photobook.dao.IPhoneDao#delete(com.coombu.photobook.model.Phone)
	 */
    @Override
    public void delete(Phone persistentInstance) {
        log.debug("removing Phone instance");
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
	 * @see com.coombu.photobook.dao.IPhoneDao#merge(com.coombu.photobook.model.Phone)
	 */
    @Override
    public Phone merge(Phone detachedInstance) {
        log.debug("merging Phone instance");
        try {
            Phone result = (Phone)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IPhoneDao#findById(int)
	 */
    public Phone findById( int id) {
        log.debug("getting Phone instance with id: " + id);
        try {
            Phone instance = (Phone) super.findById(Phone.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

