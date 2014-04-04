package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.EventTypeTable;

/**
 * Home object for domain model class EventTypeTable.
 * @see com.coombu.photobook.model.lookup.EventTypeTable
 * @author Fekade Aytaged
 */

public class EventTypeTableDao extends HibernateBaseDao implements IEventTypeTable 
		 {

	private static final Logger log = LoggerFactory
			.getLogger(EventTypeTableDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IEventTypeTable#save(com.coombu.photobook.model.lookup.EventTypeTable)
	 */
	@Override
	public void save(EventTypeTable transientInstance) {
		log.debug("persisting EventTypeTable instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IEventTypeTable#delete(com.coombu.photobook.model.lookup.EventTypeTable)
	 */
	@Override
	public void delete(EventTypeTable persistentInstance) {
		log.debug("removing EventTypeTable instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IEventTypeTable#merge(com.coombu.photobook.model.lookup.EventTypeTable)
	 */
	@Override
	public EventTypeTable merge(EventTypeTable detachedInstance) {
		log.debug("merging EventTypeTable instance");
		try {
			EventTypeTable result = (EventTypeTable)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IEventTypeTable#findById(short)
	 */
	public EventTypeTable findById(short id) {
		log.debug("getting EventTypeTable instance with id: " + id);
		try {
			EventTypeTable instance = (EventTypeTable) super.findById(
					EventTypeTable.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
