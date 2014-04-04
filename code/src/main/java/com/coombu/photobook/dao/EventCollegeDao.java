package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.model.EventCollege;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class EventCollege.
 * @see com.coombu.photobook.model.EventCollege
 * @author Fekade Aytaged
 */

public class EventCollegeDao extends HibernateBaseDao implements IEventCollegeDao  
{

    private static final Logger log = LoggerFactory.getLogger(EventCollegeDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IEventCollegeDao#save(com.coombu.photobook.model.EventCollege)
	 */
    @Override
    public void save(EventCollege transientInstance) {
        log.debug("persisting EventCollege instance");
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
	 * @see com.coombu.photobook.dao.IEventCollegeDao#delete(com.coombu.photobook.model.EventCollege)
	 */
    @Override
    public void delete(EventCollege persistentInstance) {
        log.debug("removing EventCollege instance");
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
	 * @see com.coombu.photobook.dao.IEventCollegeDao#merge(com.coombu.photobook.model.EventCollege)
	 */
    @Override
    public EventCollege merge(EventCollege detachedInstance) {
        log.debug("merging EventCollege instance");
        try {
            EventCollege result = (EventCollege)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IEventCollegeDao#findById(int)
	 */
    public EventCollege findById( int id) {
        log.debug("getting EventCollege instance with id: " + id);
        try {
            EventCollege instance = (EventCollege) super.findById(EventCollege.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

