package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.AddressType;

/**
 * Home object for domain model class AddressType.
 * @see com.coombu.photobook.model.lookup.AddressType
 * @author Fekade Aytaged
 */

public class AddressTypeDao extends HibernateBaseDao implements IAddressType {

	private static final Logger log = LoggerFactory
			.getLogger(AddressTypeDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IAddressType#save(com.coombu.photobook.model.lookup.AddressType)
	 */
	
	@Override
	public void save(AddressType transientInstance) {
		log.debug("persisting AddressType instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IAddressType#delete(com.coombu.photobook.model.lookup.AddressType)
	 */
	
	@Override
	public void delete(AddressType persistentInstance) {
		log.debug("removing AddressType instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IAddressType#merge(com.coombu.photobook.model.lookup.AddressType)
	 */
	
	@Override
	public AddressType merge(AddressType detachedInstance) {
		log.debug("merging AddressType instance");
		try {
			AddressType result = (AddressType) super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IAddressType#findById(short)
	 */
	@Override
	public AddressType findById(short id) {
		log.debug("getting AddressType instance with id: " + id);
		try {
			AddressType instance = (AddressType) super.findById(
					AddressType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
