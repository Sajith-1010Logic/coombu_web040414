package com.coombu.photobook.service;

import java.util.List;
import java.util.Set;

import org.primefaces.model.CroppedImage;

import com.coombu.photobook.dto.FriendDTO;
import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageTag;
import com.coombu.photobook.model.ImageVote;
import com.coombu.photobook.model.RemovalRequest;
import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.service.exception.CoombuServiceException;
import com.coombu.photobook.service.exception.DuplicateImageException;
import com.coombu.photobook.service.exception.DuplicateTagException;
import com.coombu.photobook.service.exception.DuplicateVoteException;
import com.coombu.photobook.service.exception.ImageServiceException;
import com.coombu.photobook.util.Constants.SUBMENU_TYPE;
import com.coombu.photobook.webservice.model.CommentRequest;
import com.coombu.photobook.webservice.model.ImageTagRequest;
import com.coombu.photobook.webservice.model.LikeRequest;

public interface IImageService {

	public Image getImage(long imageId, boolean isLoadComments);

	public void uploadPicture(Image image) throws Exception;

	public List<Image> getPendingImages(int securityUserId, int eventId);

	public void approveImages(List<Image> images);

	public void tagUsers(List<EventSecurityUser> eventSecurityUser,
			long imageId, long eventId, long securityUserId);

	public List<FriendDTO> getUsersInGroup(SecurityUser securityUser);

	public void notifyTaggedPicture(Image image, List<SecurityUser> users);

	public void voteForPicture(Image image, SecurityUser user);

	public void comment(Image image);

	public void requestImageRemoval(RemovalRequest removalRequest);

	public void RemoveComment(int commentId);

	public Image getImage(String fileName, long currentEventId);

	public void save(List<Image> fileList) throws DuplicateImageException;

	public int getEventImagesCount(SUBMENU_TYPE type, Long eventId);

	public List<ImageVote> getImageVote(long imageId);

	public List<ImageVote> getImageVoteByUser(int eventSecurityUserId);

	public List<ImageVote> getImageVoteByEvent(Long eventId);

	public List<Image> getEventImages(SUBMENU_TYPE type, long EventId,
			Integer pageNumber, Integer pageSize);

	public List<Image> getEventImages(SUBMENU_TYPE subMenuType, long eventId);

	public List<Image> getEventImages(long eventSecurityUserId, long eventId);

	public List<Image> getEventImages(SUBMENU_TYPE type,
			long eventSecurityUserId, long eventId);

	public int deleteImage(long imageId, long eventSecurityUserId);

	public void addComment(CommentRequest comment);

	public void addTag(ImageTagRequest tag) throws DuplicateTagException;

	public void addLike(LikeRequest vote) throws DuplicateVoteException;

	public List<RemovalRequest> getFlaggedComments(long eventId);

	public List<RemovalRequest> getFlaggedImages(long eventId);

	public void repost(long imageId, long eventId, short approveId)
			throws CoombuServiceException;

	public void repostComment(long commentId, long eventId, short approveId)
			throws CoombuServiceException;

	public List<Comment> getEventComments(long securityUserId, long eventId);

	public int flagImage(long imageId, long eventSecurityUserId,RemovalRequest req);

	public int flagComment(long commentId, long eventSecurityUserId,RemovalRequest req);

	/**
	 * uploads the file from web service and saves the metadata in the Image
	 * object
	 * 
	 * @param img
	 * @throws ImageServiceException
	 */
	public void uploadSavePicture(Image img) throws ImageServiceException;

	public int getImageCount(long eventSecurityId, long currentEventId);

	public int getCommentCount(long eventSecurityId, long currentEventId);

	public List<ImageTag> getImageTags(long imageId, long eventId);

	public Comment getComment(long commentId);

	public Image getImages(long imageId);

	public boolean unLike(LikeRequest request);

	public boolean checkImageLikeExistence(long imageId, long eventId,
			long eventSecUserId);

	public Comment addNewComment(Comment comment);

	public boolean checkCommentExistence(long imageId, long eventSecurityId);

	public boolean checkTagExistence(long imageId, long securityId);

	public void uploadImageWS(Image img, byte[] image)
			throws ImageServiceException;

	public boolean deleteComment(int commentId);

	public boolean reportComment(int commentId, long eventSecurityUserId);

	public boolean updateDescription(Image image);

	/**
	 * 
	 * @param imageId
	 * @return Comments that are made for this image
	 */
	public Set<Comment> getComments(long imageId);

	public int getCommentCountByImage(long imageId);
	
	public boolean checkTagExistenceForUserToThisImage(long eventSecurityId, long imageId);

	public String getHashcode(UserProfile profile, CroppedImage cropImage);

	public String getImageName(UserProfile profile);
	
	public int getLikeCountBasedOnUser(long eventSecurityId, long eventId);

	public void requestCommentRemoval(RemovalRequest req);
	
	public String getOriginallFileName(String fileName);
}
