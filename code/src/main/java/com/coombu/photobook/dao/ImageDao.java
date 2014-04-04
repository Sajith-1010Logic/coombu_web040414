package com.coombu.photobook.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.coombu.photobook.dao.exception.DuplicateImageDaoException;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageVote;
import com.coombu.photobook.model.RemovalRequest;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;
import com.coombu.photobook.util.Constants.SUBMENU_TYPE;

/**
 * Home object for domain model class Image.
 * 
 * @see com.coombu.photobook.model.Image
 * @author Fekade Aytaged
 */
@Repository
public class ImageDao extends HibernateBaseDao implements IImageDao {

	private static final Logger log = LoggerFactory.getLogger(ImageDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IImageDao#save(com.coombu.photobook.model.Image)
	 */
	@Override
	public void save(Image transientInstance) {
		log.debug("persisting Image instance");
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
	 * com.coombu.photobook.dao.IImageDao#delete(com.coombu.photobook.model.
	 * Image)
	 */
	@Override
	public void delete(Image image) {
		log.debug("removing Image instance");
		try {
			// super.delete(persistentInstance);
			// mark image deleted - physically not removed
			image.setImageStatusTypeId(IMAGE_STATUS_TYPE.DELETED.id());
			super.update(image);
			log.debug("remove successful");
			getCurrentSession().flush();
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IImageDao#merge(com.coombu.photobook.model.Image
	 * )
	 */
	@Override
	public Image merge(Image detachedInstance) {
		log.debug("merging Image instance");
		try {
			Image result = (Image) super.merge(detachedInstance);
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
	 * @see com.coombu.photobook.dao.IImageDao#findById(int)
	 */
	public Image findById(long id) {
		log.debug("getting Image instance with id: " + id);
		try {
			Image instance = (Image) super.findById(Image.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Image> getImages(SUBMENU_TYPE type, long eventId,
			Integer pageNumber, Integer pageSize) {
		List<Image> imageList = null;
		Criteria criteria = getCurrentSession().createCriteria(Image.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("event.eventId", eventId));
		criteria.add(Restrictions.eq("imageStatusTypeId",
				IMAGE_STATUS_TYPE.APPROVED.id()));
		addSubMenuCriteria(type, criteria);
		if (pageNumber != null)
			criteria.setFirstResult(pageNumber);
		if (pageSize != null)
			criteria.setMaxResults(pageSize);
		imageList = (List<Image>) criteria.list();
		if (imageList == null)
			imageList = new ArrayList<Image>();
		return imageList;

		/*
		 * List<Image> newUniqueList = new ArrayList<Image>(); Iterator it =
		 * imageList.iterator(); while (it.hasNext()) { Image img = (Image)
		 * it.next(); if (newUniqueList.contains(img)) {
		 * log.debug("image is already exists in this list! :" +
		 * img.getImageId()); } else {
		 * log.debug("adding the image in new list: " + img.getImageId());
		 * newUniqueList.add(img); } } return newUniqueList;
		 */
	}

	private void addSubMenuCriteria(SUBMENU_TYPE type, Criteria criteria) {
		switch (type) {
		case LATEST: {
			criteria.addOrder(Order.desc("uploadDateTime"));
			break;
		}
		case MOST_LIKED: {
			criteria.addOrder(Order.desc("voteCount"));
			criteria.addOrder(Order.desc("uploadDateTime"));
			break;
		}
		case MOST_COMMENTED: {
			criteria.addOrder(Order.desc("commentCount"));
			criteria.addOrder(Order.desc("uploadDateTime"));
			break;
		}
		case MOST_VIEWED: {
			criteria.addOrder(Order.desc("viewCount"));
			criteria.addOrder(Order.desc("uploadDateTime"));
			break;
		}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void addMySubMenuCriteria(SUBMENU_TYPE type, Criteria criteria,
			long eventSecurityUserId) {
		switch (type) {
		case MY_UPLOADED: {
			criteria.add(Restrictions.eq(
					"eventSecurityUser.eventSecurityUserId",
					eventSecurityUserId));
			criteria.addOrder(Order.desc("uploadDateTime"));
			break;
		}
		case MY_COMMENTS: {
			criteria.createAlias("comments", "comments");
			criteria.createAlias("comments.eventSecurityUser",
					"eventSecurityUser");
			criteria.add(Restrictions.eq(
					"eventSecurityUser.eventSecurityUserId",
					eventSecurityUserId));
			criteria.addOrder(Order.desc("uploadDateTime"));
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			break;
		}
		case MY_LIKES: {
			criteria.createAlias("imageVotes", "imageVotes");
			criteria.createAlias("imageVotes.eventSecurityUser",
					"eventSecurityUser");
			criteria.add(Restrictions.eq(
					"eventSecurityUser.eventSecurityUserId",
					eventSecurityUserId));
			criteria.addOrder(Order.desc("uploadDateTime"));
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			break;
		}
		case TAGGED: {
			criteria.createAlias("imageTags", "imageTags");
			criteria.createAlias("imageTags.eventSecurityUser",
					"eventSecurityUser");
			criteria.add(Restrictions.eq(
					"eventSecurityUser.eventSecurityUserId",
					eventSecurityUserId));
			criteria.addOrder(Order.desc("uploadDateTime"));
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			break;
		}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Image> getImages(SUBMENU_TYPE type, long eventSecurityUserId,
			long eventId) {
		List<Image> imageList = null;
		Criteria criteria = getCurrentSession().createCriteria(Image.class);
		criteria.add(Restrictions.eq("event.eventId", eventId));
		criteria.add(Restrictions.eq("imageStatusTypeId",
				IMAGE_STATUS_TYPE.APPROVED.id()));
		addMySubMenuCriteria(type, criteria, eventSecurityUserId);
		imageList = (List<Image>) criteria.list();

		List<Image> newUniqueList = new ArrayList<Image>();

		Iterator it = imageList.iterator();
		while (it.hasNext()) {
			Image img = (Image) it.next();
			if (newUniqueList.contains(img)) {
				log.debug("image is already exists in this list! :"
						+ img.getImageId());
			} else {
				log.debug("adding the image in new list: " + img.getImageId());
				newUniqueList.add(img);
			}
		}

		return newUniqueList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Image> getImages(long eventSecurityUserId, long eventId) {
		List<Image> imageList = null;
		Criteria criteria = getCurrentSession().createCriteria(Image.class);
		criteria.add(Restrictions.eq("event.eventId", eventId));
		criteria.add(Restrictions.eq("eventSecurityUser.eventSecurityUserId",
				eventSecurityUserId));
		criteria.add(Restrictions.eq("imageStatusTypeId",
				IMAGE_STATUS_TYPE.APPROVED.id()));
		imageList = (List<Image>) criteria.list();

		List<Image> newUniqueList = new ArrayList<Image>();

		Iterator it = imageList.iterator();
		while (it.hasNext()) {
			Image img = (Image) it.next();
			if (newUniqueList.contains(img)) {
				log.debug("image is already exists in this list! :"
						+ img.getImageId());
			} else {
				log.debug("adding the image in new list: " + img.getImageId());
				newUniqueList.add(img);
			}
		}

		return newUniqueList;
	}

	@Override
	public int getImagesCount(SUBMENU_TYPE type, long eventId) {
		Criteria criteria = getCurrentSession().createCriteria(Image.class);
		criteria.add(Restrictions.eq("event.eventId", eventId));
		criteria.add(Restrictions.eq("imageStatusTypeId",
				IMAGE_STATUS_TYPE.APPROVED.id()));
		addSubMenuCriteria(type, criteria);
		Long count = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return count.intValue();
	}

	@Override
	public Image getImage(String fileName, long eventId) {
		return (Image) getCurrentSession().createCriteria(Image.class)
				.add(Restrictions.eq("event.eventId", eventId))
				.add(Restrictions.eq("fileName", fileName)).uniqueResult();
	}

	@Override
	public void save(List<Image> fileList) throws DuplicateImageDaoException {
		StringBuilder errMsg = new StringBuilder("Saving failed");
		boolean errFound = false;
		for (Image img : fileList) {
			try {
				getCurrentSession().save(img);
			} catch (ConstraintViolationException ge) {
				log.debug("Error: ", ge);
				log.debug("Cause: ", ge.getCause());
				if (ge.getCause().toString().contains("USER_FILE_NAME_IDX")) {
					errMsg.append("&lt;br/&gt;")
							.append(img.getOriginalFileName())
							.append(" file already uploaded");
					errFound = true;
				} else
					throw ge;
			}
		}
		if (errFound == true) {
			errMsg.append("&lt;br/&gt;").append("Please remove");
			throw new DuplicateImageDaoException(errMsg.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ImageVote> getImageVote(long imageId) {
		List<ImageVote> voteList = null;
		voteList = (List<ImageVote>) getCurrentSession()
				.createCriteria(ImageVote.class)
				.add(Restrictions.eqOrIsNull("imageId", imageId)).list();

		return voteList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ImageVote> getImageVoteByUser(int eventSecurityUserId) {
		List<ImageVote> voteList = null;
		voteList = (List<ImageVote>) getCurrentSession()
				.createCriteria(ImageVote.class)
				.add(Restrictions.eqOrIsNull(
						"eventSecurityUser.eventSecurityUserId",
						eventSecurityUserId)).list();

		return voteList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ImageVote> getImageVoteByEvent(Long eventId) {
		List<ImageVote> voteList = null;
		voteList = (List<ImageVote>) getCurrentSession()
				.createCriteria(ImageVote.class)
				.add(Restrictions.eqOrIsNull("event.eventId", eventId))
				.setFetchMode("eventSecurityUser", FetchMode.JOIN)
				.setFetchMode("image", FetchMode.JOIN).list();
		return voteList;
	}

	@Override
	public Image getImage(long imageId, boolean isLoadComments) {
		Image img = null;

		Criteria criteria = getCurrentSession()
				.createCriteria(Image.class)
				.add(Restrictions.eq("imageId", imageId))
				.add(Restrictions.ne("imageStatusTypeId",
						IMAGE_STATUS_TYPE.DELETED.id()));
		if (isLoadComments == true) {
			criteria.setFetchMode("comments", FetchMode.JOIN);
			criteria.setFetchMode("comments.eventSecurityUser", FetchMode.JOIN);
		}
		img = (Image) criteria.uniqueResult();
		getCurrentSession().flush();
		return img;
	}

	@Override
	public int getImagesCount(long eventSecurityId, long eventId) {

		Long num = (Long) getCurrentSession()
				.createCriteria(Image.class)
				.add(Restrictions.eqOrIsNull(
						"eventSecurityUser.eventSecurityUserId",
						eventSecurityId))
				.add(Restrictions.eqOrIsNull("event.eventId", eventId))
				.add(Restrictions.eq("imageStatusTypeId", (short) 2))
				.setProjection(Projections.rowCount()).uniqueResult();
		return num.intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobook.dao.IImageDao#getAllImages(com.coombu.photobook.
	 * util.Constants.IMAGE_STATUS_TYPE, java.lang.Integer, java.lang.Integer)
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Image> getAllImages(IMAGE_STATUS_TYPE type, long eventId,
			Integer pageNumber, Integer pageSize) {
		List<Image> imageList = null;
		Criteria criteria = getCurrentSession().createCriteria(Image.class);
		criteria.add(Restrictions.eq("event.eventId", eventId));
		criteria.add(Restrictions.eq("imageStatusTypeId", type.id()));
		criteria.addOrder(Order.desc("uploadDateTime"));
		// addImageCriteria(type, criteria);
		if (pageNumber != null)
			criteria.setFirstResult(pageNumber);
		if (pageSize != null)
			criteria.setMaxResults(pageSize);
		imageList = (List<Image>) criteria.list();
		if (imageList == null)
			imageList = new ArrayList<Image>();
		if (pageNumber != null)
			criteria.setFirstResult(pageNumber);
		if (pageSize != null)
			criteria.setMaxResults(pageSize);
		imageList = (List<Image>) criteria.list();
		if (imageList == null)
			imageList = new ArrayList<Image>();

		List<Image> newUniqueList = new ArrayList<Image>();

		Iterator it = imageList.iterator();
		while (it.hasNext()) {
			Image img = (Image) it.next();
			if (newUniqueList.contains(img)) {
				log.debug("image is already exists in this list! :"
						+ img.getImageId());
			} else {
				log.debug("adding the image in new list: " + img.getImageId());
				newUniqueList.add(img);
			}
		}

		return newUniqueList;
	}

	@Override
	public int getAdminImagesCount(long eventId) {
		Criteria criteria = getCurrentSession().createCriteria(
				RemovalRequest.class);
		criteria.add(Restrictions.eq("eventId", eventId));
		Long count = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return count.intValue();
	}

	@Override
	public void update(Image image) {
		log.debug("updating Image instance");
		try {
			// super.delete(persistentInstance);
			// mark image deleted - physically not removed
			super.update(image);
			log.debug("update successful");
			getCurrentSession().flush();
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	@Override
	public int flagImage(long imageId, long eventSecurityId) {
		Image img = findById(imageId);
		if (img != null) {

			img.setImageStatusTypeId(IMAGE_STATUS_TYPE.FLAGED.id());
			super.update(img);
			log.debug("update successful");
			getCurrentSession().flush();
			return 0;
		} else {
			return 2;
		}

	}

	@Override
	public long getUploadsCountByEvent(long eventId, Date startDate,
			Date endDate) {
		log.debug("Getting image count based on the eventId");

		Criteria criteria = getCurrentSession().createCriteria(Image.class);
		criteria.add(Restrictions.eq("event.eventId", eventId));
		criteria.add(Restrictions.eq("imageStatusTypeId",
				IMAGE_STATUS_TYPE.APPROVED.id()));

		if (startDate != null) {
			criteria.add(Restrictions.ge("uploadDateTime", startDate));
		}

		if (endDate != null) {
			criteria.add(Restrictions.le("uploadDateTime", endDate));
		}

		long count = (long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();

		log.debug("total image count is :" + count);

		return count;
	}

	@Override
	public List<Object[]> getAllUsersActivityReport(long eventId,
			Date startDate, Date endDate) {

		List<Object[]> result = null;

		Criteria criteria = getCurrentSession().createCriteria(Image.class);

		criteria.add(Restrictions.eq("event.eventId", eventId));
		criteria.add(Restrictions.eq("imageStatusTypeId",
				IMAGE_STATUS_TYPE.APPROVED.id()));

		if (startDate != null) {
			criteria.add(Restrictions.ge("uploadDateTime", startDate));
		}

		if (endDate != null) {
			criteria.add(Restrictions.le("uploadDateTime", endDate));
		}

		result = criteria
				.setProjection(
						Projections
								.projectionList()
								.add(Projections
										.groupProperty("eventSecurityUser.eventSecurityUserId"))
								.add(Projections.rowCount())
								.add(Projections.sum("commentCount"))
								.add(Projections.sum("tagCount"))

				).list();

		return result;
	}

	@Override
	public Image getImages(Long ImageId) {
		Image img = null;

		Criteria criteria = getCurrentSession().createCriteria(Image.class)
				.add(Restrictions.eq("imageId", ImageId))
				.setFetchMode("comments", FetchMode.JOIN);

		img = (Image) criteria.uniqueResult();
		getCurrentSession().flush();
		return img;
	}

	@Override
	public synchronized boolean incrementImageVoteCount(long imageId) {
		try {
			Criteria cri = getCurrentSession().createCriteria(Image.class);
			Image img = (Image) cri.add(Restrictions.eq("imageId", imageId))
					.uniqueResult();
			if (img != null) {
				log.debug("Image found!");
				img.setVoteCount(img.getVoteCount() + 1);
				super.update(img);
				log.debug("incremented the vote count of the image!");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public synchronized boolean decrementImageVoteCount(long imageId) {
		try {
			Criteria cri = getCurrentSession().createCriteria(Image.class);
			Image img = (Image) cri.add(Restrictions.eq("imageId", imageId))
					.uniqueResult();
			if (img != null) {
				log.debug("Image found!");
				img.setVoteCount(img.getVoteCount() - 1);
				super.update(img);
				log.debug("decremented the vote count of the image!");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getHashCodeOfTheImage(UserProfile profile) {

		Query query = getCurrentSession()
				.createQuery(
						"select imageFileName from UserProfile where userProfileId = :id");

		query.setParameter("id", profile.getUserProfileId());

		String imageName = (String) query.uniqueResult();
		return imageName;

	}

	@Override
	public int getImageLikeCountBasedOnUser(long eventSecurityId, long eventId) {
		Criteria criteria = getCurrentSession().createCriteria(ImageVote.class)
				.createAlias("image", "img").createAlias("event", "ev")
				.createAlias("eventSecurityUser", "esu");

		criteria.add(Restrictions.eq("img.imageStatusTypeId",
				IMAGE_STATUS_TYPE.APPROVED.id()));
		criteria.add(Restrictions.eq("ev.eventId", eventId));
		criteria.add(Restrictions
				.eq("esu.eventSecurityUserId", eventSecurityId));
		List<ImageVote> votes = (List<ImageVote>) criteria.list();

		if (votes != null && !votes.isEmpty()) {
			return votes.size();
		}
		return 0;
	}
	
	@Override
	public String getOriginalFileName(String fileName) {
		try {
			Criteria cri = getCurrentSession().createCriteria(Image.class);
			Image img = (Image) cri.add(Restrictions.eq("originalFileName", fileName))
					.add(Restrictions.eq("imageStatusTypeId",IMAGE_STATUS_TYPE.APPROVED.id() ))
					.uniqueResult();
			
			return img.getOriginalFileName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
