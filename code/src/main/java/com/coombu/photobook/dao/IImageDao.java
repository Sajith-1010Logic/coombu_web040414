package com.coombu.photobook.dao;

import java.util.Date;
import java.util.List;

import com.coombu.photobook.dao.exception.DuplicateImageDaoException;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageVote;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;
import com.coombu.photobook.util.Constants.SUBMENU_TYPE;

public interface IImageDao extends IDao {

	public abstract void save(Image transientInstance);

	public abstract void delete(Image persistentInstance);

	public abstract Image merge(Image detachedInstance);

	public abstract Image findById(long id);

	public abstract List<Image> getImages(SUBMENU_TYPE type, long eventId,
			Integer pageNumber, Integer pageSize);

	public abstract Image getImage(String fileName, long eventId);

	public abstract void save(List<Image> uploadedFiles)
			throws DuplicateImageDaoException;

	public int getImagesCount(SUBMENU_TYPE type, long eventId);

	public abstract List<ImageVote> getImageVote(long imageId);

	public abstract List<ImageVote> getImageVoteByUser(int eventSecurityUserId);

	public abstract List<ImageVote> getImageVoteByEvent(Long eventId);

	public abstract Image getImage(long imageId, boolean isLoadComments);

	public abstract List<Image> getImages(long eventSecurityUserId, long eventId);

	public abstract int getImagesCount(long eventSecurityId, long eventId);

	public abstract int flagImage(long imageId, long eventSecurityId);

	/*
	 * get all images for approval
	 */
	public abstract List<Image> getAllImages(IMAGE_STATUS_TYPE type,
			long eventId, Integer pageNumber, Integer pageSize);

	public abstract List<Image> getImages(SUBMENU_TYPE type,
			long eventSecurityUserId, long eventId);

	public abstract int getAdminImagesCount(long eventId);

	public abstract void update(Image persistentInstance);

	public long getUploadsCountByEvent(long eventId, Date startDate,
			Date endDate);

	public List<Object[]> getAllUsersActivityReport(long eventId,
			Date startDate, Date endDate);

	public abstract Image getImages(Long ImageId);

	public abstract boolean incrementImageVoteCount(long imageId);

	public abstract boolean decrementImageVoteCount(long imageId);

	public String getHashCodeOfTheImage(UserProfile profile);

	public int getImageLikeCountBasedOnUser(long eventSecurityId, long eventId);
	
	public abstract String getOriginalFileName(String fileName);
}