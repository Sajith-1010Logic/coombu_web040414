package com.coombu.photobook.dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.Address;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class Address.
 * @see com.coombu.photobook.model.Address
 * @author Fekade Aytaged
 */
@Repository
public class AddressDao extends HibernateBaseDao implements IAddressDao 
{

    private static final Logger log = LoggerFactory.getLogger(AddressDao.class);

    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IAddressDao#save(com.coombu.photobook.model.Address)
	 */
    @Override
    public void save(Address transientInstance) {
        log.debug("persisting Address instance");
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
	 * @see com.coombu.photobook.dao.IAddressDao#delete(com.coombu.photobook.model.Address)
	 */
    @Override
    public void delete(Address persistentInstance) {
        log.debug("removing Address instance");
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
	 * @see com.coombu.photobook.dao.IAddressDao#merge(com.coombu.photobook.model.Address)
	 */
    @Override
    public Address merge(Address detachedInstance) {
        log.debug("merging Address instance");
        try {
            Address result = (Address)super.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.coombu.photobook.dao.IAddressDao#findById(int)
	 */
    public Address findById( int id) {
        log.debug("getting Address instance with id: " + id);
        try {
            Address instance = (Address) super.findById(Address.class, id);
            log.debug("get successful");
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

