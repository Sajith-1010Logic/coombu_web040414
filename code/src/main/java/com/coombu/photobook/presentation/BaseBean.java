package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageTag;
import com.coombu.photobook.model.RemovalRequest;
import com.coombu.photobook.service.IImageService;
import com.coombu.photobook.service.IUserService;
import com.coombu.photobook.service.exception.DuplicateVoteException;
import com.coombu.photobook.util.Constants.EVENT_STATUS_TYPE;
import com.coombu.photobook.util.Constants.REQUEST_REMOVAL_TYPE;
import com.coombu.photobook.util.Constants.RESOLUTION_STATUS_TYPE;
import com.coombu.photobook.util.Constants.ROLE;
import com.coombu.photobook.util.Constants.USER_STATUS_TYPE;
import com.coombu.photobook.util.CoombuUtil;
import com.coombu.photobook.webservice.model.LikeRequest;

public class BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	public GroupBean groupBean;

	@Autowired
	public IUserService userService;

	@Autowired
	public IImageService imageService;

	private Logger log = LoggerFactory.getLogger(BaseBean.class);

	public List<Image> imageList;

	public long currentEventId;

	public long eventSecurityId;

	public int currentImageIndex;

	public String commentText;

	public Integer flagReasonId = 0;

	public String flagText;

	private List<EventSecurityUser> taggedUserIds;

	/************************************************************************************************
	 * 
	 * Getter and Setters
	 *
	 ***********************************************************************************************/
	/**
	 * @return the groupBean
	 */
	public GroupBean getGroupBean() {
		return groupBean;
	}

	/**
	 * @param groupBean
	 *            the groupBean to set
	 */
	public void setGroupBean(GroupBean groupBean) {
		this.groupBean = groupBean;
	}

	/**
	 * @return the userService
	 */
	public IUserService getUserService() {
		return userService;
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the imageService
	 */
	public IImageService getImageService() {
		return imageService;
	}

	/**
	 * @param imageService
	 *            the imageService to set
	 */
	public void setImageService(IImageService imageService) {
		this.imageService = imageService;
	}

	/**
	 * @return the imageList
	 */
	public List<Image> getImageList() {
		return imageList;
	}

	/**
	 * @param imageList
	 *            the imageList to set
	 */
	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}

	/**
	 * @return the currentEventId
	 */
	public long getCurrentEventId() {
		return currentEventId;
	}

	/**
	 * @param currentEventId
	 *            the currentEventId to set
	 */
	public void setCurrentEventId(long currentEventId) {
		this.currentEventId = currentEventId;
	}

	/**
	 * @return the eventSecurityId
	 */
	public long getEventSecurityId() {
		return eventSecurityId;
	}

	/**
	 * @param eventSecurityId
	 *            the eventSecurityId to set
	 */
	public void setEventSecurityId(long eventSecurityId) {
		this.eventSecurityId = eventSecurityId;
	}

	/**
	 * @return the currentImageIndex
	 */
	public int getCurrentImageIndex() {
		return currentImageIndex;
	}

	/**
	 * @param currentImageIndex
	 *            the currentImageIndex to set
	 */
	public void setCurrentImageIndex(int currentImageIndex) {
		this.currentImageIndex = currentImageIndex;
	}

	/**
	 * @return the commentText
	 */
	public String getCommentText() {
		return commentText;
	}

	/**
	 * @param commentText
	 *            the commentText to set
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	/**
	 * @return the flagReasonId
	 */
	public int getFlagReasonId() {
		return flagReasonId;
	}

	/**
	 * @param flagReasonId
	 *            the flagReasonId to set
	 */
	public void setFlagReasonId(int flagReasonId) {
		this.flagReasonId = flagReasonId;
	}

	/**
	 * @return the flagText
	 */
	public String getFlagText() {
		return flagText;
	}

	/**
	 * @param flagText
	 *            the flagText to set
	 */
	public void setFlagText(String flagText) {
		this.flagText = flagText;
	}

	public List<EventSecurityUser> getTaggedUserIds() {
		if (taggedUserIds == null) {
			taggedUserIds = new ArrayList<EventSecurityUser>();
		} else {
			taggedUserIds.clear();
			if (currentImageIndex >= imageList.size()) {
				return taggedUserIds;
			}
			Image img = imageList.get(currentImageIndex);
			if (img != null) {
				List<ImageTag> tags = null;
				if (!Hibernate.isInitialized(img.getImageTags())) {
					tags = imageService.getImageTags(img.getImageId(),
							currentEventId);
					img.setImageTags(tags);
				} else {
					tags = img.getImageTags();
				}
				for (ImageTag tag : tags) {
					taggedUserIds.add(tag.getEventSecurityUser());
				}
			}
		}

		log.debug("total size of the tagged users " + taggedUserIds.size());
		return taggedUserIds;
	}

	public void setTaggedUserIds(List<EventSecurityUser> taggedUserId) {
		this.taggedUserIds = taggedUserId;
		log.debug("total value while setting is: " + this.taggedUserIds.size());
	}

	/*********************************************************************************
	 * Operations
	 *
	 *********************************************************************************/
	protected void handleError(String string) {
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.getApplication().getNavigationHandler()
				.handleNavigation(fc, null, "unauthorized");
	}

	public boolean isStudentRep() {
		return getCurrentEventSecurityUser().getRoleId() == ROLE.ROLE_REP.id() ? true
				: false;
	}

	protected boolean isEventIdAllowedAccess(long currentEventId2) {
		for (EventSecurityUser esu : groupBean.getUserEvents()) {
			if (currentEventId2 == esu.getEvent().getEventId()
					&& esu.getEvent().getEventStatus() == EVENT_STATUS_TYPE.ACTIVE
							.id()
					&& esu.getUserStatusTypeId() == USER_STATUS_TYPE.ACTIVE
							.id())
				return true;
		}
		return false;
	}

	public EventSecurityUser getCurrentEventSecurityUser() {
		EventSecurityUser eventSecurityUser = null;
		for (EventSecurityUser seu : this.groupBean.getUserEvents()) {
			if (seu.getEvent().getEventId() == this.currentEventId) {
				eventSecurityUser = seu;
				break;
			}
		}
		return eventSecurityUser;

	}

	public void addComment() {
		log.debug("base been comment is called!");
		/*
		 * CommentRequest comment = new CommentRequest(currentEventId, imageList
		 * .get(currentImageIndex).getImageId(),
		 * getCurrentEventSecurityUser().getEventSecurityUserId(),
		 * this.commentText, getCurrentEventSecurityUser()
		 * .getSecurityUser().getSecurityUserId());
		 * imageService.addComment(comment);
		 */
		if (commentText == null || (commentText.trim()).length() <= 0) {
			FacesContext.getCurrentInstance().addMessage(
					"commentTxt",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Please enter your comment",
							"Please enter your comment"));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("wgtCommentDialog.show()");
			context.execute("wgtTagDialog.hide()");
			context.execute("wgtFlagDialog.hide()");
		

			return;
		}

		Comment comment = new Comment();
		comment.setCommentStatusId((short) 1);
		comment.setCommentText(commentText);
		comment.setCreatedBy(getCurrentEventSecurityUser().getSecurityUser()
				.getSecurityUserId());
		comment.setEventSecurityUser(getCurrentEventSecurityUser());
		comment.setImage(imageList.get(currentImageIndex));
		comment.setPositiveVoteCount(0);
		comment.setNegativeVoteCount(0);

		Comment tempComment = imageService.addNewComment(comment);

		if (tempComment != null && tempComment.getCommentText() != null) {
			log.debug("Comment is added!");
			imageList.get(currentImageIndex).setCommentCount(
					imageList.get(currentImageIndex).getCommentCount() + 1);
			// imageList.get(currentImageIndex).getComments().add(comment);
		} else {
			log.debug("Error while adding new Comment!");
		}

		comment = null;
		tempComment = null;
		this.commentText = "";
	}

	public void addLike(long imageId) {
		log.debug("like function is called!");
		LikeRequest like = new LikeRequest(currentEventId, imageId,
				eventSecurityId, getCurrentEventSecurityUser()
						.getSecurityUser().getSecurityUserId(), null);
		try {
			imageService.addLike(like);
			for (Image img : imageList) {
				if (img.getImageId() == imageId) {
					int count = img.getVoteCount();
					img.setVoteCount(++count);
					break;
				}
			}
		} catch (DuplicateVoteException dve) {
			/*
			 * FacesContext.getCurrentInstance().addMessage( null, new
			 * FacesMessage(FacesMessage.SEVERITY_INFO, "Already Liked",
			 * "Already Liked"));
			 */
			log.debug("Exception blog!");
			if (imageService.unLike(like)) {

				for (Image img : imageList) {
					if (img.getImageId() == imageId) {
						int count = img.getVoteCount();
						if (count > 0)
							img.setVoteCount(--count);
						break;
					}
				}

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"You have unliked this image!", ""));
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error while performing unlike to this image!",
								""));
			}

		}
	}

	public void addTag() {
		long imageId = imageList.get(currentImageIndex).getImageId();
		// ImageTagRequest tag = new ImageTagRequest(currentEventId, imageId ,
		// taggedUserId,
		// getCurrentEventSecurityUser().getSecurityUser().getSecurityUserId());

		try {

			log.debug("current eventId:" + currentEventId);
			log.debug("current eventId:" + getCurrentEventId());

			log.debug("total selected user is:" + taggedUserIds.size());

			imageService.tagUsers(taggedUserIds, imageId, currentEventId,
					CoombuUtil.getSecurityUserId());

			imageList.set(currentImageIndex, imageService.getImages(imageId));

			/*
			 * if (taggedUserId.size() > 0) { for (Image img : imageList) { if
			 * (img.getImageId() == imageId) { img.setTagCount(img.getTagCount()
			 * + taggedUserId.size()); break; } } }else{ FacesContext
			 * .getCurrentInstance() .addMessage( null, new FacesMessage(
			 * FacesMessage.SEVERITY_WARN,
			 * "You have already tagged these users with this picture!", "")); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error occured while tagging, please try again!",
							""));
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("wgtTagDialog.hide()");
	}

	public void flag() {

		log.debug("flag reason and flag text: " + flagReasonId + " and "
				+ flagText);
		if (flagReasonId == null || flagReasonId <= 0) {
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Please Choose a reason",
									"Please choose a reason"));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("wgtCommentDialog.hide()");
			context.execute("wgtTagDialog.hide()");
			context.execute("wgtFlagDialog.show()");
			
			return;
		}

		long imageId = imageList.get(currentImageIndex).getImageId();
		RemovalRequest req = new RemovalRequest(this
				.getCurrentEventSecurityUser().getSecurityUser()
				.getUserProfile(), REQUEST_REMOVAL_TYPE.IMAGE.id(),
				currentEventId, imageId, this.flagReasonId,
				RESOLUTION_STATUS_TYPE.PENDING.id(), this.flagText);
		imageService.requestImageRemoval(req);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("wgtTagDialog.hide()");
		flagReasonId = 0;
		flagText = "";

		Image img = imageList.get(currentImageIndex);

		if (imageList.remove(img)) {
			log.debug("image is removed from the list!");
		}
		log.debug("Flag is added!");

	}
	public void flagComment(long commentId) {

		log.debug("flag reason and flag text: " + flagReasonId + " and "
				+ flagText);
		if (flagReasonId == null || flagReasonId <= 0) {
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Please Choose a reason",
									"Please choose a reason"));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("wgtCommentDialog.hide()");
			context.execute("wgtTagDialog.hide()");
			context.execute("wgtCommentFlagDialog.show()");
			
			return;
		}

		long imageId = imageList.get(currentImageIndex).getImageId();
		RemovalRequest req = new RemovalRequest(this.getCurrentEventSecurityUser().getSecurityUser().getUserProfile(), 
				REQUEST_REMOVAL_TYPE.COMMENT.id(),
				currentEventId, commentId, this.flagReasonId,
				RESOLUTION_STATUS_TYPE.PENDING.id(), this.flagText);
		imageService.requestCommentRemoval(req);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("wgtTagDialog.hide()");
		flagReasonId = 0;
		flagText = "";
		log.debug("Comment flag is added!");

	}

	public int delete(int index) {

		long imageId = imageList.get(index).getImageId();
		int id = imageService.deleteImage(imageId, eventSecurityId);

		
		return id;
	}

	public void unLike(long imageId) {
		LikeRequest like = new LikeRequest(currentEventId, imageId,
				eventSecurityId, getCurrentEventSecurityUser()
						.getSecurityUser().getSecurityUserId(), null);
		if (imageService.unLike(like)) {

			for (Image img : imageList) {
				if (img.getImageId() == imageId) {
					int count = img.getVoteCount();
					img.setVoteCount(--count);
					break;
				}
			}

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"You have unliked this image!", ""));
		} else {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Error while performing unlike to this image!",
									""));
		}

	}

}
