package com.coombu.photobook.dao;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.CollegeRegistrationRequest;

/**
 * Home object for domain model class CollegeRegistrationRequest.
 * @see com.coombu.photobook.model.CollegeRegistrationRequest
 * @author Fekade Aytaged
 */
@Repository
public class CollegeRegistrationRequestDao extends HibernateBaseDao implements ICollegeRegistrationRequestDao 
{

    private static final Logger log = LoggerFactory.getLogger(CollegeRegistrationRequestDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.ICollegeRegistrationRequestDao#save(com.coombu.photobook.model.CollegeRegistrationRequest)
	 */
    @Override
    public void save(CollegeRegistrationRequest transientInstance) {
        log.debug("persisting CollegeRegistrationRequest instance");
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
	 * @see com.coombu.photobook.dao.ICollegeRegistrationRequestDao#delete(com.coombu.photobook.model.CollegeRegistrationRequest)
	 */
    @Override
    public void delete(CollegeRegistrationRequest persistentInstance) {
        log.debug("removing CollegeRegistrationRequest instance");
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
	 * @see com.coombu.photobook.dao.ICollegeRegistrationRequestDao#merge(com.coombu.photobook.model.CollegeRegistrationRequest)
	 */
    @Override
    public CollegeRegistrationRequest merge(CollegeRegistrationRequest detachedInstance) {
        log.debug("merging CollegeRegistrationRequest instance");
        try {
            CollegeRegistrationRequest result = (CollegeRegistrationRequest)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.ICollegeRegistrationRequestDao#findById(int)
	 */
    public CollegeRegistrationRequest findById( int id) {
        log.debug("getting CollegeRegistrationRequest instance with id: " + id);
        try {
            CollegeRegistrationRequest instance = (CollegeRegistrationRequest) super.findById(CollegeRegistrationRequest.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

