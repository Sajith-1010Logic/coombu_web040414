package com.coombu.photobook.dao;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.RemovalRequest;
import com.coombu.photobook.util.Constants.REQUEST_REMOVAL_TYPE;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class RemovalRequest.
 * @see com.coombu.photobook.model.RemovalRequest
 * @author Fekade Aytaged
 */

@Repository
public class RemovalRequestDao extends HibernateBaseDao implements IRemovalRequestDao 
{

    private static final Logger log = LoggerFactory.getLogger(RemovalRequestDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IRemovalRequestDao#save(com.coombu.photobook.model.RemovalRequest)
	 */
    @Override
    public void save(RemovalRequest transientInstance) {
        log.debug("persisting RemovalRequest instance");
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
	 * @see com.coombu.photobook.dao.IRemovalRequestDao#delete(com.coombu.photobook.model.RemovalRequest)
	 */
    @Override
    public void delete(RemovalRequest persistentInstance) {
        log.debug("removing RemovalRequest instance");
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
	 * @see com.coombu.photobook.dao.IRemovalRequestDao#merge(com.coombu.photobook.model.RemovalRequest)
	 */
    @Override
    public RemovalRequest merge(RemovalRequest detachedInstance) {
        log.debug("merging RemovalRequest instance");
        try {
            RemovalRequest result = (RemovalRequest)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IRemovalRequestDao#findById(int)
	 */
    public RemovalRequest findById( int id) {
        log.debug("getting RemovalRequest instance with id: " + id);
        try {
            RemovalRequest instance = (RemovalRequest) super.findById(RemovalRequest.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
	@SuppressWarnings("unchecked")
	@Override
	public List<RemovalRequest> getFlaggedComments(long eventId) 
	{
		
		return (List<RemovalRequest>) getCurrentSession().createCriteria(RemovalRequest.class)
			.add(Restrictions.eq("eventId", eventId))
			.add(Restrictions.eq("removalRequestTypeId", REQUEST_REMOVAL_TYPE.COMMENT.id()))
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.list();
				
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<RemovalRequest> getFlaggedImages(long eventId) 
	{
		
		return (List<RemovalRequest>) getCurrentSession().createCriteria(RemovalRequest.class)
			.add(Restrictions.eq("eventId", eventId))
			.add(Restrictions.eq("removalRequestTypeId", REQUEST_REMOVAL_TYPE.IMAGE.id()))
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.list();
		
		
	}
	
	@Override
	public RemovalRequest findByCommentId(int id) {
		  log.debug("getting RemovalRequest instance with id: " + id);
	        try {
	        	
	        	
	         @SuppressWarnings("unchecked")
			List <RemovalRequest> instance =(List<RemovalRequest>) getCurrentSession().createCriteria(RemovalRequest.class)
	        			.add(Restrictions.eq("imageCommentId", (long)id)).list();
	            
	            log.debug("get successful");
	            if(instance.size()>0)
	            return instance.get(0);
	            else
	            	return null;
	        }
	        catch (RuntimeException re) {
	            log.error("get failed", re);
	            throw re;
	        }
	}

	@Override
	public void repostComment(long removalRequestId) 
	{
		String sql = "delete from removal_request where removal_request_id = :removalRequestId";
		getCurrentSession().createSQLQuery(sql)
			.setParameter("removalRequestId", removalRequestId)
			.executeUpdate();
	}
	
}

