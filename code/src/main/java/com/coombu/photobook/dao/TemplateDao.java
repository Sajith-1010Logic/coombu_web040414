package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.model.Template;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class Template.
 * @see com.coombu.photobook.model.Template
 * @author Fekade Aytaged
 */

public class TemplateDao extends HibernateBaseDao implements ITemplateDao 
{

    private static final Logger log = LoggerFactory.getLogger(TemplateDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.ITemplateDao#save(com.coombu.photobook.model.Template)
	 */
    @Override
    public void save(Template transientInstance) {
        log.debug("persisting Template instance");
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
	 * @see com.coombu.photobook.dao.ITemplateDao#delete(com.coombu.photobook.model.Template)
	 */
    @Override
    public void delete(Template persistentInstance) {
        log.debug("removing Template instance");
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
	 * @see com.coombu.photobook.dao.ITemplateDao#merge(com.coombu.photobook.model.Template)
	 */
    @Override
    public Template merge(Template detachedInstance) {
        log.debug("merging Template instance");
        try {
            Template result = (Template)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.ITemplateDao#findById(short)
	 */
    public Template findById( short id) {
        log.debug("getting Template instance with id: " + id);
        try {
            Template instance = (Template) super.findById(Template.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

