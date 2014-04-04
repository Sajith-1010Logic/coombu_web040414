package com.coombu.photobook.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.util.Constants.COMMENT_STATUS_TYPE;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;
import com.coombu.photobook.webservice.model.CommentRequest;

// Generated Nov 3, 2013 6:30:54 PM by Hibernate Tools 4.0.0

/**
 * Home object for domain model class Comment.
 * 
 * @see com.coombu.photobook.model.Comment
 * @author Fekade Aytaged
 */
@Repository
public class CommentDao extends HibernateBaseDao implements ICommentDao {

	private static final Logger log = LoggerFactory.getLogger(CommentDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.ICommentDao#save(com.coombu.photobook.model.
	 * Comment)
	 */
	@Override
	public void save(Comment transientInstance) {
		log.debug("persisting Comment instance");
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
	 * com.coombu.photobook.dao.ICommentDao#save(com.coombu.photobook.model.
	 * Comment)
	 */
	@Override
	public void saveWS(CommentRequest comment) {
		log.debug("persisting Comment instance");
		try {
			String sql = "INSERT INTO comment (EVENT_SECURITY_USER_ID,IMAGE_ID,parent_comment_id,COMMENT_TEXT,COMMENT_STATUS_ID,POSITIVE_VOTE_COUNT,NEGATIVE_VOTE_COUNT,CREATE_TMSTMP,CREATED_BY)"
					+ "VALUES (?,?,NULL,?,1,0,0,NOW(),?)";
			getCurrentSession().createSQLQuery(sql)
					.setLong(0, comment.getEventSecurityUserId())
					.setLong(1, comment.getImageId())
					.setString(2, comment.getCommentText())
					.setLong(3, comment.getSecurityUserId()).executeUpdate();

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
	 * com.coombu.photobook.dao.ICommentDao#delete(com.coombu.photobook.model
	 * .Comment)
	 */
	@Override
	public void delete(Comment persistentInstance) {
		log.debug("removing Comment instance");
		try {
			if (persistentInstance != null) {
				persistentInstance
						.setCommentStatusId(COMMENT_STATUS_TYPE.DELETED.id());
				update(persistentInstance);
				log.debug("comment status is changed to delete!");
			}
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
	 * com.coombu.photobook.dao.ICommentDao#merge(com.coombu.photobook.model
	 * .Comment)
	 */
	@Override
	public Comment merge(Comment detachedInstance) {
		log.debug("merging Comment instance");
		try {
			Comment result = (Comment) super.merge(detachedInstance);
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
	 * @see com.coombu.photobook.dao.ICommentDao#findById(int)
	 */
	public Comment findById(int id) {
		log.debug("getting Comment instance with id: " + id);
		try {
			Comment instance = (Comment) super.findById(Comment.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public int getCommentCount(long eventSecurityId, long eventId) {

		SQLQuery query = getCurrentSession()
				.createSQLQuery(
						"select count(*) from comment where event_security_user_id = ? and comment_status_id=? and image_id in (select image_id from image where event_id = ?)");
		query.setLong(0, eventSecurityId);
		query.setLong(1, 1);
		query.setLong(2, eventId);
		Number num = (Number) query.uniqueResult();
		return num.intValue();
	}

	@Override
	public Comment getAllComments(long commentId) {
		try {
			Comment instance = (Comment) super.findById(Comment.class,
					(int) commentId);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<Comment> getComments(long eventSecurityId, long eventId) {
		List<Comment> commentList = null;
		Criteria criteria = getCurrentSession().createCriteria(Comment.class);
		// criteria.add(Restrictions.eq("eventSecurityUser.eventSecurityUserId",
		// eventSecurityId));
		// criteria.add(Restrictions.eq("commentStatusId",
		// IMAGE_STATUS_TYPE.APPROVED.id()));
		commentList = (List<Comment>) criteria.list();
		if (commentList == null)
			commentList = new ArrayList<Comment>();
		return commentList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Comment> getFlagComments(long eventId, long eventSecurityId) {
		List<Comment> commentList = null;
		log.debug("getting the flagged comments listed under this group!");
		Criteria criteria = getCurrentSession().createCriteria(Comment.class)
				.createAlias("image", "img");
		criteria.add(Restrictions.eq("img.event.eventId", eventId));
		criteria.add(Restrictions.eq("commentStatusId",
				COMMENT_STATUS_TYPE.FLAGED.id()));
		criteria.setFetchMode("img", FetchMode.JOIN);
		commentList = (List<Comment>) criteria.list();
		if (commentList == null)
			commentList = new ArrayList<Comment>();
		return commentList;

	}

	@Override
	public int flagComment(long commentId, long eventSecurityUserId) {
		Comment cmt = findById((int) commentId);
		if (cmt != null) {

			cmt.setCommentStatusId(COMMENT_STATUS_TYPE.FLAGED.id());
			super.update(cmt);
			log.debug("update successful");
			getCurrentSession().flush();
			return 0;
		} else {
			return 2;
		}
	}

	@Override
	public Long getAllCommentsCount(Long eventId, Date startDate, Date endDate) {
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
			criteria = getCurrentSession().createCriteria(Comment.class);

			criteria.add(Restrictions.in("image.imageId", resultIds));
			criteria.add(Restrictions.eq("commentStatusId",
					COMMENT_STATUS_TYPE.ACTIVE.id()));

			if (startDate != null) {
				criteria.add(Restrictions.ge("createTimestamp", startDate));
			}

			if (endDate != null) {
				criteria.add(Restrictions.le("createTimestamp", endDate));
			}

			Long count = (Long) criteria.setProjection(Projections.rowCount())
					.uniqueResult();

			log.debug("count of comments: " + count);

			return count;
		}

		log.debug("No comments available for usage report!");

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkCommentExistence(long imageId, long eventSecurityId) {
		Criteria cri = getCurrentSession().createCriteria(Comment.class);
		cri.add(Restrictions.eq("image.imageId", imageId));
		cri.add(Restrictions.eq("commentStatusId",
				COMMENT_STATUS_TYPE.ACTIVE.id()));
		List<Comment> comment = (List<Comment>) cri.add(
				Restrictions.eq("eventSecurityUser.eventSecurityUserId",
						eventSecurityId)).list();

		if (comment != null && comment.size() > 0) {
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Comment> getComments(long imageId) {
		List<Comment> commentList = (List<Comment>) getCurrentSession()
				.createCriteria(Comment.class)
				.add(Restrictions.eq("image.imageId", imageId)).list();
		return new LinkedHashSet<Comment>(commentList);

	}

	@Override
	public int getCommentCountByImageId(long imageId) {
		// TODO Auto-generated method stub
		List<Comment> commentList = (List<Comment>) getCurrentSession()
				.createCriteria(Comment.class)
				.add(Restrictions.eq("commentStatusId",
						COMMENT_STATUS_TYPE.ACTIVE.id()))
				.add(Restrictions.eq("image.imageId", imageId)).list();
		if (commentList != null && !commentList.isEmpty()) {
			return commentList.size();
		}
		return 0;
	}

	@Override
	public boolean deleteCommentsForThisImage(long imageId) {
		List<Comment> commentList = (List<Comment>) getCurrentSession()
				.createCriteria(Comment.class)
				.add(Restrictions.eq("commentStatusId",
						COMMENT_STATUS_TYPE.ACTIVE.id()))
				.add(Restrictions.eq("image.imageId", imageId)).list();
		log.debug("Deleting comments for the imageId");
		int count = 0, commentCount = 0;
		commentCount = commentList.size();
		for (Comment comment : commentList) {
			comment.setCommentStatusId(COMMENT_STATUS_TYPE.IMAGE_DELETED.id());
			update(comment);
			count += 1;
		}
		if (count == commentCount) {
			log.debug("comments for the image are deleted properly!");
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean inActivateCommentsForThisImage(long imageId) {
		List<Comment> commentList = (List<Comment>) getCurrentSession()
				.createCriteria(Comment.class)
				.add(Restrictions.eq("commentStatusId",
						COMMENT_STATUS_TYPE.ACTIVE.id()))
				.add(Restrictions.eq("image.imageId", imageId)).list();
		log.debug("Updating comments to inactive for the imageid");
		int count = 0, commentCount = 0;
		commentCount = commentList.size();
		for (Comment comment : commentList) {
			comment.setCommentStatusId(COMMENT_STATUS_TYPE.IMAGE_FLAGGED.id());
			update(comment);
			count += 1;
		}
		if (count == commentCount) {
			log.debug("comments for the image are updated properly!");
			return true;
		}
		return false;
	}

	@Override
	public boolean activateAllTheCommentsForThisImage(long imageId) {
		List<Comment> commentList = (List<Comment>) getCurrentSession()
				.createCriteria(Comment.class)
				.add(Restrictions.eq("commentStatusId",
						COMMENT_STATUS_TYPE.IMAGE_FLAGGED.id()))
				.add(Restrictions.eq("image.imageId", imageId)).list();
		log.debug("Updating comments to active for the imageid");
		int count = 0, commentCount = 0;
		commentCount = commentList.size();
		for (Comment comment : commentList) {
			comment.setCommentStatusId(COMMENT_STATUS_TYPE.ACTIVE.id());
			update(comment);
			count += 1;
		}
		if (count == commentCount) {
			log.debug("comments for the image are updated properly!");
			return true;
		}
		return false;
	}
}
