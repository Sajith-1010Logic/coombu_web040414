package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.model.Book;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class Book.
 * @see com.coombu.photobook.model.Book
 * @author Fekade Aytaged
 */

public class BookDao extends HibernateBaseDao implements IBookDao 
{

    private static final Logger log = LoggerFactory.getLogger(BookDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IBookDao#save(com.coombu.photobook.model.Book)
	 */
    @Override
    public void save(Book transientInstance) {
        log.debug("persisting Book instance");
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
	 * @see com.coombu.photobook.dao.IBookDao#delete(com.coombu.photobook.model.Book)
	 */
    @Override
    public void delete(Book persistentInstance) {
        log.debug("removing Book instance");
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
	 * @see com.coombu.photobook.dao.IBookDao#merge(com.coombu.photobook.model.Book)
	 */
    @Override
    public Book merge(Book detachedInstance) {
        log.debug("merging Book instance");
        try {
            Book result = (Book)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IBookDao#findById(int)
	 */
    public Book findById( int id) {
        log.debug("getting Book instance with id: " + id);
        try {
            Book instance = (Book) super.findById(Book.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

