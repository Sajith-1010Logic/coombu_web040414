package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.model.EventTemplate;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class EventTemplate.
 * @see com.coombu.photobook.model.EventTemplate
 * @author Fekade Aytaged
 */

public class EventTemplateDao extends HibernateBaseDao implements IEventTemplateDao 
{

    private static final Logger log = LoggerFactory.getLogger(EventTemplateDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IEventTemplateDao#save(com.coombu.photobook.model.EventTemplate)
	 */
    @Override
    public void save(EventTemplate transientInstance) {
        log.debug("persisting EventTemplate instance");
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
	 * @see com.coombu.photobook.dao.IEventTemplateDao#delete(com.coombu.photobook.model.EventTemplate)
	 */
    @Override
    public void delete(EventTemplate persistentInstance) {
        log.debug("removing EventTemplate instance");
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
	 * @see com.coombu.photobook.dao.IEventTemplateDao#merge(com.coombu.photobook.model.EventTemplate)
	 */
    @Override
    public EventTemplate merge(EventTemplate detachedInstance) {
        log.debug("merging EventTemplate instance");
        try {
            EventTemplate result = (EventTemplate)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IEventTemplateDao#findById(int)
	 */
    public EventTemplate findById( int id) {
        log.debug("getting EventTemplate instance with id: " + id);
        try {
            EventTemplate instance = (EventTemplate) super.findById(EventTemplate.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

