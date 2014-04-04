package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.model.PasswordHistory;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class PasswordHistory.
 * @see com.coombu.photobook.model.PasswordHistory
 * @author Fekade Aytaged
 */

public class PasswordHistoryDao extends HibernateBaseDao implements IPasswordHistoryDao 
{

    private static final Logger log = LoggerFactory.getLogger(PasswordHistoryDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IPasswordHistoryDao#save(com.coombu.photobook.model.PasswordHistory)
	 */
    @Override
    public void save(PasswordHistory transientInstance) {
        log.debug("persisting PasswordHistory instance");
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
	 * @see com.coombu.photobook.dao.IPasswordHistoryDao#delete(com.coombu.photobook.model.PasswordHistory)
	 */
    @Override
    public void delete(PasswordHistory persistentInstance) {
        log.debug("removing PasswordHistory instance");
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
	 * @see com.coombu.photobook.dao.IPasswordHistoryDao#merge(com.coombu.photobook.model.PasswordHistory)
	 */
    @Override
    public PasswordHistory merge(PasswordHistory detachedInstance) {
        log.debug("merging PasswordHistory instance");
        try {
            PasswordHistory result = (PasswordHistory)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IPasswordHistoryDao#findById(int)
	 */
    public PasswordHistory findById( int id) {
        log.debug("getting PasswordHistory instance with id: " + id);
        try {
            PasswordHistory instance = (PasswordHistory) super.findById(PasswordHistory.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

