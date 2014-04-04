package com.coombu.photobook.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.ImageBucketImage;
import com.coombu.photobook.model.Image;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.model.ImageBucket;
import com.coombu.photobook.util.Constants.IMAGE_BUCKET_STATUS_TYPE;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;

/**
 * 
 * 
 *
 */
@Repository
public class PhotoBucketDao extends HibernateBaseDao implements IPhotoBucketDao {

	private static final Logger log = LoggerFactory
			.getLogger(PhotoBucketDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IBookDao#save(com.coombu.photobook.model.Book)
	 */
	@Override
	public int save(ImageBucket transientInstance) {
		log.debug("persisting Book instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
			return 1;
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			return 0;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IBookDao#delete(com.coombu.photobook.model.Book)
	 */
	@Override
	public void delete(ImageBucket persistentInstance) {
		log.debug("removing Book instance");
		try {
			super.delete(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IBookDao#merge(com.coombu.photobook.model.Book)
	 */
	@Override
	public ImageBucket merge(ImageBucket detachedInstance) {
		log.debug("merging Book instance");
		try {
			ImageBucket result = (ImageBucket) super.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coombu.photobook.dao.IBookDao#findById(int)
	 */
	public ImageBucket findById(int id) {
		log.debug("getting Book instance with id: " + id);
		try {
			ImageBucket instance = (ImageBucket) super.findById(
					ImageBucket.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<ImageBucket> findByEventId(int eventId) {
		log.debug("getting Book instance with eventid: " + eventId);
		try {
			@SuppressWarnings("unchecked")
			List<ImageBucket> imageBucket = getCurrentSession()
					.createCriteria(ImageBucket.class)
					.add(Restrictions.eq("eventID", eventId)).list();
			return imageBucket;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			return null;
		}
	}

	@Override
	public int saveAlbumImage(ImageBucketImage albumImage) {
		log.debug("persisting Book instance");
		try {
			super.save(albumImage);
			log.debug("persist successful");
			return 1;
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			return 0;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ImageBucketImage> getAlbumImages(int eventID, int type) {

		List<ImageBucket> imageBucketList = new ArrayList<ImageBucket>();
		imageBucketList = getCurrentSession().createCriteria(ImageBucket.class)
				.add(Restrictions.eq("eventID", eventID))
				.add(Restrictions.eq("imageStatusType", type)).list();
		if (imageBucketList.size() > 0) {
			ImageBucket imBucket = imageBucketList.get(0);
			List<ImageBucketImage> imageList = getCurrentSession()
					.createCriteria(ImageBucketImage.class)
					.add(Restrictions.eq("albumName", imBucket)).list();
			return imageList;
		}
		return null;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ImageBucketImage> getPartialySavedImages(int currentEventId) {
		List<ImageBucket> imageBucketList = new ArrayList<ImageBucket>();
		imageBucketList = getCurrentSession()
				.createCriteria(ImageBucket.class)
				.add(Restrictions.eq("eventID", currentEventId))
				.add(Restrictions.eq("imageStatusType",
						IMAGE_BUCKET_STATUS_TYPE.PARTIALSAVE.id())).list();
		if (imageBucketList.size() > 0) {
			ImageBucket imBucket = imageBucketList.get(0);
			List<ImageBucketImage> imageList = getCurrentSession()
					.createCriteria(ImageBucketImage.class)
					.add(Restrictions.eq("albumName", imBucket)).list();
			return imageList;
		}

		return null;
	}

	@Override
	public void removeImageIdsFromBucket(int ImageId) {

		Long id = (long) ImageId;

		Query query = getCurrentSession().createQuery(
				"delete from ImageBucketImage where imageName.imageId =:id");
		query.setParameter("id", id);
		query.executeUpdate();

	}

}
