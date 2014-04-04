package com.coombu.photobook.dao.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coombu.photobook.dao.HibernateBaseDao;
// Generated Nov 3, 2013 12:48:31 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.lookup.FileType;

/**
 * Home object for domain model class FileType.
 * @see com.coombu.photobook.model.lookup.FileType
 * @author Fekade Aytaged
 */

public class FileTypeDao extends HibernateBaseDao implements IFileType   {

	private static final Logger log = LoggerFactory
			.getLogger(FileTypeDao.class);

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IFileType#save(com.coombu.photobook.model.lookup.FileType)
	 */
	@Override
	public void save(FileType transientInstance) {
		log.debug("persisting FileType instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IFileType#delete(com.coombu.photobook.model.lookup.FileType)
	 */
	@Override
	public void delete(FileType persistentInstance) {
		log.debug("removing FileType instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IFileType#merge(com.coombu.photobook.model.lookup.FileType)
	 */
	@Override
	public FileType merge(FileType detachedInstance) {
		log.debug("merging FileType instance");
		try {
			FileType result = (FileType)super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.dao.lookup.IFileType#findById(short)
	 */
	public FileType findById(short id) {
		log.debug("getting FileType instance with id: " + id);
		try {
			FileType instance = (FileType) super.findById(FileType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
