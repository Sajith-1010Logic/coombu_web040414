package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.model.CommentVote;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class CommentVote.
 * @see com.coombu.photobook.model.CommentVote
 * @author Fekade Aytaged
 */

public class CommentVoteDao extends HibernateBaseDao implements ICommentVoteDao 
{

    private static final Logger log = LoggerFactory.getLogger(CommentVoteDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.ICommentVoteDao#save(com.coombu.photobook.model.CommentVote)
	 */
    @Override
    public void save(CommentVote transientInstance) {
        log.debug("persisting CommentVote instance");
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
	 * @see com.coombu.photobook.dao.ICommentVoteDao#delete(com.coombu.photobook.model.CommentVote)
	 */
    @Override
    public void delete(CommentVote persistentInstance) {
        log.debug("removing CommentVote instance");
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
	 * @see com.coombu.photobook.dao.ICommentVoteDao#merge(com.coombu.photobook.model.CommentVote)
	 */
    @Override
    public CommentVote merge(CommentVote detachedInstance) {
        log.debug("merging CommentVote instance");
        try {
            CommentVote result = (CommentVote)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.ICommentVoteDao#findById(int)
	 */
    public CommentVote findById( int id) {
        log.debug("getting CommentVote instance with id: " + id);
        try {
            CommentVote instance = (CommentVote) super.findById(CommentVote.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

