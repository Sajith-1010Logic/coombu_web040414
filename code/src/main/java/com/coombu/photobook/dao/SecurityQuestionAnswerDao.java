package com.coombu.photobook.dao;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.SecurityQuestionAnswer;

/**
 * Home object for domain model class SecurityQuestionAnswer.
 * @see com.coombu.photobook.model.SecurityQuestionAnswer
 * @author Fekade Aytaged
 */
@Repository
public class SecurityQuestionAnswerDao extends HibernateBaseDao implements ISecurityQuestionAnswerDao 
{

    private static final Logger log = LoggerFactory.getLogger(SecurityQuestionAnswerDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.ISecurityQuestionAnswerDao#save(com.coombu.photobook.model.SecurityQuestionAnswer)
	 */
    @Override
    public void save(SecurityQuestionAnswer transientInstance) {
        log.debug("persisting SecurityQuestionAnswer instance");
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
	 * @see com.coombu.photobook.dao.ISecurityQuestionAnswerDao#delete(com.coombu.photobook.model.SecurityQuestionAnswer)
	 */
    @Override
    public void delete(SecurityQuestionAnswer persistentInstance) {
        log.debug("removing SecurityQuestionAnswer instance");
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
	 * @see com.coombu.photobook.dao.ISecurityQuestionAnswerDao#merge(com.coombu.photobook.model.SecurityQuestionAnswer)
	 */
    @Override
    public SecurityQuestionAnswer merge(SecurityQuestionAnswer detachedInstance) {
        log.debug("merging SecurityQuestionAnswer instance");
        try {
            SecurityQuestionAnswer result = (SecurityQuestionAnswer)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.ISecurityQuestionAnswerDao#findById(int)
	 */
    public SecurityQuestionAnswer findById( int id) {
        log.debug("getting SecurityQuestionAnswer instance with id: " + id);
        try {
            SecurityQuestionAnswer instance = (SecurityQuestionAnswer) super.findById(SecurityQuestionAnswer.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

