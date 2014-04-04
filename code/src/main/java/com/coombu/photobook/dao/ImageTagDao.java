package com.coombu.photobook.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.dao.exception.DuplicateTagDaoException;
import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.ImageTag;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.webservice.model.ImageTagRequest;

/**
 * Home object for domain model class ImageTag.
 * 
 * @see com.coombu.photobook.model.ImageTag
 * @author Fekade Aytaged
 */
@Repository
public class ImageTagDao extends HibernateBaseDao implements IImageTagDao {

	private static final Logger log = LoggerFactory
			.getLogger(ImageTagDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IImageTagDao#save(com.coombu.photobook.model
	 * .ImageTag)
	 */
	@Override
	public void save(ImageTag transientInstance) {
		log.debug("persisting ImageTag instance");
		try {
			super.save(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IImageTagDao#delete(com.coombu.photobook.model
	 * .ImageTag)
	 */
	@Override
	public void delete(ImageTag persistentInstance) {
		log.debug("removing ImageTag instance");
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
	 * com.coombu.photobook.dao.IImageTagDao#merge(com.coombu.photobook.model
	 * .ImageTag)
	 */
	@Override
	public ImageTag merge(ImageTag detachedInstance) {
		log.debug("merging ImageTag instance");
		try {
			ImageTag result = (ImageTag) super.merge(detachedInstance);
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
	 * @see com.coombu.photobook.dao.IImageTagDao#findById(int)
	 */
	public ImageTag findById(int id) {
		log.debug("getting ImageTag instance with id: " + id);
		try {
			ImageTag instance = (ImageTag) super.findById(ImageTag.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public void saveWS(ImageTagRequest tag) throws DuplicateTagDaoException {
		log.debug("inserting an IMAGE_TAG");
		try {
			String sql = "INSERT INTO image_tag (IMAGE_ID, EVENT_ID, EVENT_SECURITY_USER_ID, CREATE_TMSTMP,  CREATED_BY) "
					+ "VALUES (?,?,?,now(),?)";

			getCurrentSession().createSQLQuery(sql)
					.setLong(0, tag.getImageId()).setLong(1, tag.getEventId())
					.setLong(2, tag.getEventSecurityUserId())
					.setLong(3, tag.getSecurityUserId()).executeUpdate();

			log.debug("insert successful");
		} catch (ConstraintViolationException cve) {
			log.error("duplicate tag:", cve);
			throw new DuplicateTagDaoException("dupicate tag");
		} catch (RuntimeException re) {
			log.error("insert failed", re);
			throw re;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveTagList(List<EventSecurityUser> userIdList, long imageId,
			long eventId, long securityUserId) {
		
		log.debug("save tag list is called!");

		String listSql = "SELECT EVENT_SECURITY_USER_ID from image_tag where IMAGE_ID = :imageId and EVENT_ID = :eventId";
		List<Integer> tagList = (List<Integer>) getCurrentSession()
				.createSQLQuery(listSql).setParameter("imageId", imageId)
				.setParameter("eventId", eventId).list();
		// ArrayList<Long> saveList = new ArrayList<Long>();
		Iterator<EventSecurityUser> it = userIdList.iterator();
		while (it.hasNext()) {
			Long userId = it.next().getEventSecurityUserId();
			for (Integer tagUserId : tagList) {
				if (tagUserId.intValue() == userId.intValue()) {
					it.remove();
					if (removeThisTag(imageId, eventId, userId)) {
						log.debug("tag is removed successfully!");
					}
					break;
				}
			}
		}

		log.debug(userIdList.size() + " user(s) are the only users available!");

		if (userIdList.isEmpty())
			return;

		String saveSql = "INSERT INTO image_tag (IMAGE_ID, EVENT_SECURITY_USER_ID, EVENT_ID, CREATE_TMSTMP, CREATED_BY) "
				+ "VALUES (:imageId,:eventSecurityId,:eventId,now(),:createdBy)";
		SQLQuery query = getCurrentSession().createSQLQuery(saveSql);
		for (EventSecurityUser user : userIdList) {
			log.debug("inside the for loop!");
			query.setParameter("imageId", imageId);
			query.setParameter("eventSecurityId", user.getEventSecurityUserId());
			query.setParameter("eventId", eventId);
			query.setParameter("createdBy", securityUserId);
			query.executeUpdate();
			log.debug("loop query executed!");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ImageTag> getImageTags(long imageId, long eventId) {
		return (List<ImageTag>) getCurrentSession()
				.createCriteria(ImageTag.class)
				.add(Restrictions.eq("image.imageId", imageId))
				.add(Restrictions.eqOrIsNull("event.eventId", eventId))
				.setFetchMode("eventSecurityUser", FetchMode.JOIN).list();
	}

	@Override
	public boolean checkTagExistence(long imageId, long securityId) {
		Criteria cri = getCurrentSession().createCriteria(ImageTag.class);
		cri.add(Restrictions.eq("image.imageId", imageId));
		List<ImageTag> tag = (List<ImageTag>) cri.add(
				Restrictions.eq("createdBy", securityId)).list();
		if (tag != null && tag.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeThisTag(long imageId, long eventId,
			long eventSecurityUserId) {
		log.debug("removing tag! ");
		Criteria cri = getCurrentSession().createCriteria(ImageTag.class);
		cri.add(Restrictions.eq("image.imageId", imageId));
		ImageTag tag = (ImageTag) cri.add(
				Restrictions.eq("eventSecurityUserId", eventSecurityUserId))
				.uniqueResult();
		if (tag != null) {
			delete(tag);
			log.debug("tag is deleted for the user :" + eventSecurityUserId
					+ " on the image: " + imageId);
			return true;
		}
		return false;
	}

	@Override
	public boolean checkThisUserAlreadyBeenTaggedWithThisImage(long imageId,
			long eventSecUserId) {
		Criteria cri = getCurrentSession().createCriteria(ImageTag.class);
		cri.add(Restrictions.eq("image.imageId", imageId));
		List<ImageTag> tag = (List<ImageTag>) cri.add(
				Restrictions.eq("eventSecurityUserId", eventSecUserId)).list();
		if (tag != null && tag.size() > 0) {
			return true;
		}
		return false;
	}
}
