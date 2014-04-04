package com.coombu.photobook.dao;

import java.util.List;
import java.util.Set;

import com.coombu.photobook.dao.exception.DuplicateTagDaoException;
import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.ImageTag;
import com.coombu.photobook.webservice.model.ImageTagRequest;

public interface IImageTagDao extends IDao {

	public abstract void save(ImageTag transientInstance);

	public abstract void saveWS(ImageTagRequest transientInstance)
			throws DuplicateTagDaoException;

	public abstract void delete(ImageTag persistentInstance);

	public abstract ImageTag merge(ImageTag detachedInstance);

	public abstract ImageTag findById(int id);

	public abstract void saveTagList(
			List<EventSecurityUser> eventSecurityUserList, long imageId,
			long eventId, long securityUserId);

	public abstract List<ImageTag> getImageTags(long imageId, long eventId);

	public abstract boolean checkTagExistence(long imageId, long securityId);

	public boolean removeThisTag(long imageId, long eventId,
			long eventSecurityUserId);
	
	public boolean checkThisUserAlreadyBeenTaggedWithThisImage(long imageId,long userId);

}