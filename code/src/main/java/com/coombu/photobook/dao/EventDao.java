package com.coombu.photobook.dao;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.Event;

/**
 * Home object for domain model class Event.
 * @see com.coombu.photobook.model.Event
 * @author Fekade Aytaged
 */
@Repository
public class EventDao extends HibernateBaseDao implements IEventDao 
{

    private static final Logger log = LoggerFactory.getLogger(EventDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IEventDao#save(com.coombu.photobook.model.Event)
	 */
    @Override
    public void save(Event transientInstance) {
        log.debug("persisting Event instance");
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
	 * @see com.coombu.photobook.dao.IEventDao#delete(com.coombu.photobook.model.Event)
	 */
    @Override
    public void delete(Event persistentInstance) {
        log.debug("removing Event instance");
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
	 * @see com.coombu.photobook.dao.IEventDao#merge(com.coombu.photobook.model.Event)
	 */
    @Override
    public Event merge(Event detachedInstance) {
        log.debug("merging Event instance");
        try {
            Event result = (Event)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IEventDao#findById(int)
	 */
    public Event findById( long id) {
        log.debug("getting Event instance with id: " + id);
        try {
            Event instance = (Event) super.findById(Event.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

