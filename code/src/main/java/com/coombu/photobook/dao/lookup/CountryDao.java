package com.coombu.photobook.dao.lookup;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.Country;

/**
 * Home object for domain model class Country.
 * @see com.coombu.photobook.model.lookup.Country
 * @author Fekade Aytaged
 */

public class CountryDao extends HibernateBaseDao implements ICountryDao   {

	private static final Logger log = LoggerFactory.getLogger(CountryDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.ICountry#save(com.coombu.photobook.model.lookup.Country)
	 */
	@Override
	public void save(Country transientInstance) {
		log.debug("persisting Country instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.ICountry#delete(com.coombu.photobook.model.lookup.Country)
	 */
	@Override
	public void delete(Country persistentInstance) {
		log.debug("removing Country instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.ICountry#merge(com.coombu.photobook.model.lookup.Country)
	 */
	@Override
	public Country merge(Country detachedInstance) {
		log.debug("merging Country instance");
		try {
			Country result = (Country)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.ICountry#findById(short)
	 */
	public Country findById(short id) {
		log.debug("getting Country instance with id: " + id);
		try {
			Country instance = (Country) super.findById(Country.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Country> getAll() {
		return (List<Country>)getCurrentSession().createQuery("FROM Country");
	}
}
