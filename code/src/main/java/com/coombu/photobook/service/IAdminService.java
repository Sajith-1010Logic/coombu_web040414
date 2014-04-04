package com.coombu.photobook.service;

/**
 * Updated code on 16-Feb-2014 for adding new functionalities
 * by sajith-1010logic
 */
import java.util.List;

import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.RemovalRequest;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;

public interface IAdminService {

	public List<RemovalRequest> getImageRemovalRequest(long eventId);

	public void approveImageRemoval(List<RemovalRequest> removalRequst);

	public List<RemovalRequest> getCommentRemovalRequest(long eventId);

	public void approveCommentRemoval(List<RemovalRequest> removalRequst);

	/*
	 * get the images for the approval of administrator
	 */
	public List<Image> getApprovalImages(IMAGE_STATUS_TYPE type,
			Integer pageNumber, long eventId, Integer pageSize);
	
	public List<Comment> getFlaggedComments(long eventId,long eventSecurityId);

	public int getImagesCount(long l);

	public void deleteImage(long imageId);

	public void repostImage(long imageId);
}
