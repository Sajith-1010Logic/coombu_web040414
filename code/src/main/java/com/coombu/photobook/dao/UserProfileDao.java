package com.coombu.photobook.dao;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.UserProfile;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class UserProfile.
 * @see com.coombu.photobook.model.UserProfile
 * @author Fekade Aytaged
 */
@Repository
public class UserProfileDao extends HibernateBaseDao implements IUserProfileDao 
{

    private static final Logger log = LoggerFactory.getLogger(UserProfileDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IUserProfileDao#save(com.coombu.photobook.model.UserProfile)
	 */
    @Override
    public void save(UserProfile transientInstance) {
        log.debug("persisting UserProfile instance");
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
	 * @see com.coombu.photobook.dao.IUserProfileDao#delete(com.coombu.photobook.model.UserProfile)
	 */
    @Override
    public void delete(UserProfile persistentInstance) {
        log.debug("removing UserProfile instance");
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
	 * @see com.coombu.photobook.dao.IUserProfileDao#merge(com.coombu.photobook.model.UserProfile)
	 */
    @Override
    public UserProfile merge(UserProfile detachedInstance) {
        log.debug("merging UserProfile instance");
        try {
            UserProfile result = (UserProfile)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IUserProfileDao#findById(int)
	 */
    public UserProfile findById( int id) {
        log.debug("getting UserProfile instance with id: " + id);
        try {
            UserProfile instance = (UserProfile) super.findById(UserProfile.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

	@Override
	public UserProfile getUserProfileBySecurityUserId(long securityUserId) {
		
		UserProfile profile = (UserProfile)getCurrentSession().createCriteria(UserProfile.class)
				.add(Restrictions.eq("securityUser.securityUserId", securityUserId))
				.uniqueResult();
		
		return profile;
	}
}

