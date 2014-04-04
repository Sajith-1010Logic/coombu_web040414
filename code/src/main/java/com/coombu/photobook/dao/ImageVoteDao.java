package com.coombu.photobook.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.dao.exception.DuplicateVoteDaoException;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageVote;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;
// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0
import com.coombu.photobook.webservice.model.LikeRequest;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * Home object for domain model class ImageVote.
 * 
 * @see com.coombu.photobook.model.ImageVote
 * @author Fekade Aytaged
 */
@Repository
public class ImageVoteDao extends HibernateBaseDao implements IImageVoteDao {

	private static final Logger log = LoggerFactory
			.getLogger(ImageVoteDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IImageVoteDao#save(com.coombu.photobook.model
	 * .ImageVote)
	 */
	@Override
	public void save(ImageVote transientInstance) {
		log.debug("persisting ImageVote instance");
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
	 * com.coombu.photobook.dao.IImageVoteDao#delete(com.coombu.photobook.model
	 * .ImageVote)
	 */
	@Override
	public void delete(ImageVote persistentInstance) {
		log.debug("removing ImageVote instance");
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
	 * com.coombu.photobook.dao.IImageVoteDao#merge(com.coombu.photobook.model
	 * .ImageVote)
	 */
	@Override
	public ImageVote merge(ImageVote detachedInstance) {
		log.debug("merging ImageVote instance");
		try {
			ImageVote result = (ImageVote) super.merge(detachedInstance);
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
	 * @see com.coombu.photobook.dao.IImageVoteDao#findById(int)
	 */
	public ImageVote findById(int id) {
		log.debug("getting ImageVote instance with id: " + id);
		try {
			ImageVote instance = (ImageVote) super
					.findById(ImageVote.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public void saveWS(LikeRequest like) throws DuplicateVoteDaoException {
		log.debug("inserting an IMAGE_VOTE");
		try {
			String sql = "INSERT INTO image_vote (IMAGE_ID, EVENT_ID, EVENT_SECURITY_USER_ID, VOTE_TYPE_ID, CREATE_TMSTMP,  CREATED_BY) "
					+ "VALUES (?, ?, ?, 1, now(), ?)";

			log.debug("image id: " + like.getImageId());
			log.debug("image event id: " + like.getEventId());
			log.debug("image event security userid: "
					+ like.getEventSecurityUserId());
			log.debug("image security userid: " + like.getSecurityUserId());

			getCurrentSession().createSQLQuery(sql)
					.setLong(0, like.getImageId())
					.setLong(1, like.getEventId())
					.setLong(2, like.getEventSecurityUserId())
					.setLong(3, like.getSecurityUserId()).executeUpdate();
			log.debug("insert successful");
		} catch (org.hibernate.exception.ConstraintViolationException cve) {
			log.error("duplicate vote:", cve);
			throw new DuplicateVoteDaoException("dupicate vote - imageId: "
					+ like.getImageId());
		} catch (RuntimeException re) {
			log.error("insert failed", re);
			throw re;
		}

	}

	@Override
	public Long getAllImageVoteCount(Long eventId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Image.class);
		criteria.add(Restrictions.eq("imageStatusTypeId",
				IMAGE_STATUS_TYPE.APPROVED.id()));

		if (startDate != null) {
			criteria.add(Restrictions.ge("uploadDateTime", startDate));
		}

		if (endDate != null) {
			criteria.add(Restrictions.le("uploadDateTime", endDate));
		}

		List<Image> imageIds = criteria.add(
				Restrictions.eq("event.eventId", eventId)).list();

		List<Long> resultIds = new ArrayList<Long>(imageIds.size());

		for (Image img : imageIds) {
			resultIds.add(img.getImageId());
		}

		log.debug("List of image ids: " + resultIds.toString());

		if (resultIds != null && resultIds.size() != 0) {

			criteria = getCurrentSession().createCriteria(ImageVote.class);

			criteria.add(Restrictions.in("image.imageId", resultIds));

			criteria.add(Restrictions.eq("voteTypeId", 1));

			if (startDate != null) {
				criteria.add(Restrictions.ge("createTimestamp", startDate));
			}

			if (endDate != null) {
				criteria.add(Restrictions.le("createTimestamp", endDate));
			}

			Long count = (Long) criteria.setProjection(Projections.rowCount())
					.uniqueResult();

			log.debug("count of image votes: " + count);

			return count;
		}

		log.debug("No comments available for usage report!");

		return null;
	}

	@Override
	public boolean unLike(LikeRequest likeRequest) {
		// TODO Auto-generated method stub
		ImageVote vote = checkImageLikeExistence(likeRequest.getImageId(),
				likeRequest.getEventId(), likeRequest.getEventSecurityUserId());

		if (vote != null) {
			getCurrentSession().delete(vote);
			log.debug("you have unliked this image!");
			return true;
		}
		log.debug("Error while unliking image!");
		return false;
	}

	@Override
	public ImageVote checkImageLikeExistence(long imageId, long eventId,
			long eventSecUserId) {

		log.debug("Current image id: " + imageId);
		log.debug("Current image eventId :" + eventId);
		log.debug("Current event securityId: " + eventSecUserId);

		ImageVote vote = (ImageVote) getCurrentSession()
				.createCriteria(ImageVote.class)
				.add(Restrictions.eq("image.imageId", imageId))
				.add(Restrictions.eq("eventSecurityUser.eventSecurityUserId",
						eventSecUserId))
				.add(Restrictions.eq("event.eventId", eventId)).uniqueResult();

		if (vote != null) {
			log.debug("you have already liked this picture!");
			return vote;
		}
		return null;
	}
}
