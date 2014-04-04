package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.service.IImageService;
import com.coombu.photobook.service.exception.DuplicateVoteException;
import com.coombu.photobook.util.Constants.COMMENT_STATUS_TYPE;
import com.coombu.photobook.webservice.model.LikeRequest;

@Named("dashImageBean")
@Scope(value = "view")
public class DashBoardImageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private DashBean dashBean;

	@Autowired
	private ActivityBean activityBean;

	String activeBean;

	private String commentText;

	private Logger log = LoggerFactory.getLogger(DashBoardImageBean.class);

	private long currentImageId;

	private int currentIndex;

	private Image currentImage;

	private List<Image> currentImageList;

	private String currentImageUri;

	private int totalImages;

	private boolean editable;

	private String flagText;

	private int flagReasonId;

	private String flagCommentText;

	private int flagCommentReasonId;

	private Comment flaggedComment;
	
	private List<EventSecurityUser> taggedUserIds;

	@Autowired
	private IImageService imageService;

	private Comment currentDeletableComment;

	public List<EventSecurityUser> getTaggedUserIds() {
		if (activeBean.equals("dashBean")) {
			taggedUserIds = dashBean.getTaggedUserIds();
		} else if (activeBean.equals("activityBean")) {
			taggedUserIds = activityBean.getTaggedUserIds();
		}
		return taggedUserIds;
	}

	public void setTaggedUserIds(List<EventSecurityUser> taggedUserIds) {
		this.taggedUserIds = taggedUserIds;
	}

	public String getFlagText() {
		return flagText;
	}

	public void setFlagText(String flagText) {
		this.flagText = flagText;
	}

	public int getFlagReasonId() {
		return flagReasonId;
	}

	public void setFlagReasonId(int flagReasonId) {
		this.flagReasonId = flagReasonId;
	}

	public String getFlagCommentText() {
		return flagCommentText;
	}

	public void setFlagCommentText(String flagCommentText) {
		this.flagCommentText = flagCommentText;
	}

	/**
	 * @return the flagCommentReasonId
	 */
	public int getFlagCommentReasonId() {
		return flagCommentReasonId;
	}

	/**
	 * @param flagCommentReasonId the flagCommentReasonId to set
	 */
	public void setFlagCommentReasonId(int flagCommentReasonId) {
		this.flagCommentReasonId = flagCommentReasonId;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Comment getCurrentDeletableComment() {
		return currentDeletableComment;
	}

	public void setCurrentDeletableComment(Comment currentDeletableComment) {
		this.currentDeletableComment = currentDeletableComment;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public int getTotalImages() {
		return totalImages;
	}

	public void setTotalImages(int totalImages) {
		this.totalImages = totalImages;
	}

	public void setCurrentImageUri(String currentImageUri) {
		this.currentImageUri = currentImageUri;
	}

	public String getCurrentImageUri() {
		return currentImageUri;
	}

	public List<Image> getCurrentImageList() {
		return currentImageList;
	}

	public void setCurrentImageList(List<Image> currentImageList) {
		this.currentImageList = currentImageList;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
		StringBuffer uri = new StringBuffer("/image?type=lightbox&id=");
		uri.append(this.currentImageList.get(this.currentIndex).getFileName());
		this.currentImageUri = uri.toString();

		log.debug("current image URI and index is setted!"
				+ this.currentImageUri + " => " + this.currentIndex);
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public DashBean getDashBean() {
		return dashBean;
	}

	public void setDashBean(DashBean dashBean) {
		this.dashBean = dashBean;
	}

	public Comment getFlaggedComment() {
		return flaggedComment;
	}

	public void setFlaggedComment(Comment flaggedComment) {
		this.flaggedComment = flaggedComment;
	}

	public long getCurrentImageId() {
		return currentImageId;
	}

	public void setCurrentImageId(long currentImageId) {
		this.currentImageId = currentImageId;
	}

	public Image getCurrentImage() {
		return currentImage;
	}

	public void setCurrentImage(Image image) {
		this.currentImage = image;
	}

	public IImageService getImageService() {
		return imageService;
	}

	public void setImageService(IImageService imageService) {
		this.imageService = imageService;
	}

	@PostConstruct
	public void init() {
		String imageId = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("componentId");
		String currentIndex = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("currentIndex");
		activeBean = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("activeBean");

		log.debug("current image id and the current index is:" + imageId
				+ " and " + currentIndex);

		loadBeanImageList();

		log.debug("current image list is:" + currentImageList.size());

		totalImages = currentImageList.size();

		if (imageId != null) {
			currentImageId = Long.parseLong(imageId);
			setCurrentIndex(Integer.parseInt(currentIndex));

			log.debug("the active component id is:" + currentImageId);
			log.debug("the active component index is:" + this.currentIndex);

			currentImage = currentImageList.get(this.currentIndex);

			log.debug("Image is " + currentImage.getFileName());

			// getting images with comments
			currentImage = imageService.getImages(currentImage.getImageId());

			log.debug("Current image name:" + currentImage.getFileName());
			log.debug("Current image comments count:"
					+ currentImage.getComments().size());

		}

	}

	public String goToNext() {
		log.debug("Going to the previous Image!");
		this.currentIndex += 1;

		if (currentIndex >= totalImages) {
			setCurrentIndex(0);
			log.debug("index is wrappered to first because of exceeding index limit!");
		} else {
			setCurrentIndex(currentIndex);
			log.debug("there is no problem becasue of index.");
		}

		currentImage = currentImageList.get(currentIndex);

		// getting images with comments
		currentImage = imageService.getImages(currentImage.getImageId());

		log.debug("current image is loaded!");

		return null;
	}

	public String goToPrevious() {
		log.debug("Going to the next Image!");
		this.currentIndex -= 1;

		if (currentIndex < 0) {
			setCurrentIndex(currentImageList.size() - 1);
			log.debug("index is wrappered to last because of index value is in minus!");

		} else {
			setCurrentIndex(currentIndex);
			log.debug("there is no problem becasue of index.");
		}

		currentImage = currentImageList.get(currentIndex);

		// getting images with comments
		currentImage = imageService.getImages(currentImage.getImageId());

		log.debug("current image is loaded!");

		return null;
	}

	public String like() {
		log.debug("perform Like is called!");

		LikeRequest like = new LikeRequest(dashBean.getCurrentEventId(),
				currentImage.getImageId(), dashBean.getEventSecurityId(),
				dashBean.getCurrentEventSecurityUser().getSecurityUser()
						.getSecurityUserId(), null);

		try {
			imageService.addLike(like);

			int count = currentImage.getVoteCount();
			currentImage.setVoteCount(++count);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"You have liked this image!", ""));
		} catch (DuplicateVoteException dve) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"You have already liked this image!",
							"Already Liked"));
		}
		return null;
	}

	public String unLike() {
		log.debug("perform Unlike is called!");

		LikeRequest like = new LikeRequest(dashBean.getCurrentEventId(),
				currentImage.getImageId(), dashBean.getEventSecurityId(),
				dashBean.getCurrentEventSecurityUser().getSecurityUser()
						.getSecurityUserId(), null);

		if (imageService.unLike(like)) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"You have unliked this image!", ""));
			int count = currentImage.getVoteCount();
			currentImage.setVoteCount(--count);
		} else {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_INFO,
									"Error while performing unlike of this image!",
									""));
		}

		return null;
	}

	public void tagImage() {
		log.debug("tagging functions start!");
		setCurrentBeanListIndex(currentIndex);

		log.debug("Current image list:" + currentImageList.size()
				+ " and current index is: " + currentIndex);

		addTag();

		log.debug("Current image list:" + currentImageList.size()
				+ " and current index is: " + currentIndex);
		if (currentImageList != null) {
			currentImageList.set(currentIndex,
					getImageBasedOnIndex(currentIndex));
			currentImage = getImageBasedOnIndex(currentIndex);
			totalImages = currentImageList.size();
		}

		log.debug("tagging functions end!");

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("wgtTagDialog.hide()");
	}

	public void deleteImage() {
		log.debug("Image Deletion is called!");
		int res = imageService.deleteImage(currentImage.getImageId(),
				dashBean.getEventSecurityId());
		if (res == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"You have deleted the current Image!", ""));

			Iterator it = currentImageList.iterator();
			while (it.hasNext()) {
				Image img = (Image) it.next();
				if (img.getImageId() == currentImage.getImageId()) {
					currentImageList.remove(img);
					log.debug("image removed!");
					break;
				}
			}

			if (!currentImageList.isEmpty()) {
				currentIndex = 0;
				currentImage = currentImageList.get(0);
				setCurrentIndex(currentIndex);
				totalImages = currentImageList.size();
			} else {
				currentImage = null;
			}

			log.debug("deletion is success!");

		} else if (res == 2) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"You are not authorized to delete this Image!",
									""));

		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error in deletion please try again!", ""));

		}

		log.debug("Image deletion is reached its functions end!");

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("deleteDialog.hide();");
	}

	public void flag() {

		setCurrentBeanListIndex(currentIndex);

		doFlag();

		Iterator it = currentImageList.iterator();
		while (it.hasNext()) {
			Image img = (Image) it.next();
			if (img.getImageId() == currentImage.getImageId()) {
				currentImageList.remove(img);
				log.debug("image removed!");
				break;
			}
		}

		if (!currentImageList.isEmpty()) {
			currentIndex = 0;
			currentImage = currentImageList.get(0);
			setCurrentIndex(currentIndex);
			totalImages = currentImageList.size();
		} else {
			currentImage = null;
		}

		log.debug("flagging is success!");

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("wgtFlagDialog.hide();");
	}

	public String addNewComment() {

		if (commentText == null || (commentText.trim()).length() <= 0) {
			log.debug("comment is empty - will not be posted");
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Please enter your comment",
							"Please enter your comment"));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('deleteCommentDialog').hide()");
			return null;
		}

		log.debug("Adding new comment!");
		Comment comment = new Comment();
		comment.setCommentStatusId((short) 1);
		comment.setCommentText(commentText);
		comment.setCreatedBy(dashBean.getCurrentEventSecurityUser()
				.getSecurityUser().getSecurityUserId());
		comment.setEventSecurityUser(dashBean.getCurrentEventSecurityUser());
		comment.setImage(currentImageList.get(currentIndex));
		comment.setPositiveVoteCount(0);
		comment.setNegativeVoteCount(0);

		Comment tempComment = imageService.addNewComment(comment);

		if (tempComment != null && tempComment.getCommentText() != null) {
			log.debug("Comment is added!");
			/*
			 * currentImageList.get(currentIndex).setCommentCount(
			 * currentImageList.get(currentIndex).getCommentCount() + 1);
			 * currentImageList.get(currentIndex).getComments().add(comment);
			 * currentImage = currentImageList.get(currentIndex);
			 */
			currentImage = imageService.getImages(currentImage.getImageId());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"You have added new comment!", ""));
		} else {
			log.debug("Error while adding new Comment!");
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Error occured while adding new comment please try again!",
									""));
		}

		comment = null;
		tempComment = null;
		this.commentText = "";
		return null;
	}

	public String deleteThisComment(Comment comment) {

		log.debug("comment deletion is called and the current comment id is : "
				+ comment != null ? comment.getCommentId() + ""
				: " this comment does not have an id!");

		if (imageService.deleteComment(comment.getCommentId())) {
			currentImage = imageService.getImages(currentImage.getImageId());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"You have deleted your own comment!", ""));
		} else {
			log.debug("Error while deleting new Comment!");
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Error occured while deleting new comment please try again!",
									""));
		}
		return null;
	}

	public String deleteCurrentComment() {
		log.debug("comment deletion is called and the current comment id is : "
				+ currentDeletableComment != null ? currentDeletableComment
				.getCommentId() + "" : " this comment does not have an id!");

		if (imageService.deleteComment(currentDeletableComment.getCommentId())) {
			currentImage = imageService.getImages(currentImage.getImageId());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"You have deleted your own comment!", ""));
		} else {
			log.debug("Error while deleting new Comment!");
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Error occured while deleting new comment please try again!",
									""));
		}
		return null;
	}

	public String reportThisComment() {
		log.debug("comment flagging is called and the current comment id is : "
				+ flaggedComment != null ? flaggedComment.getCommentId() + ""
				: " this comment does not have an id!");
		if (flagCommentReasonId <= 0 )
		{
			FacesContext
			.getCurrentInstance()
			.addMessage(
					null,
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Please select a reason",
							""));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("wgtCommentFlagDialog.show()");
			return null;
		}
		try
		{
			doCommentFlag(flaggedComment.getCommentId());
			currentImage = imageService.getImages(currentImage.getImageId());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"You have flagged one of comment of this picture!",
							""));
			
			Iterator<Comment> it = currentImage.getComments().iterator();
			while(it.hasNext())
			{
				Comment comment = it.next();
				if ( comment.getCommentId() == flaggedComment.getCommentId())
				{
					it.remove();
					break;
				}
			}
		}
		catch(Exception e)
		{
			log.error("Error while flagging Comment id: {}", flaggedComment.getCommentId(), e);
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Error occured while flagging your comment please try again!",
									""));
		}		
		flaggedComment = null;
		flagCommentText = null;
		flagReasonId = 0;
		return null;
	}

	public String editDescription() {
		log.debug("The new description is :"
				+ currentImage.getEventDescription());
		if (imageService.updateDescription(currentImage)) {
			log.debug("image event description is updated!");
			editable = false;
		}
		return null;
	}

	public String getCommentCount() {
		int count = 0;
		if (!Hibernate.isInitialized(currentImage.getComments())) {
			currentImage.setComments(imageService.getComments(currentImage
					.getImageId()));
		}
		for (Comment comment : currentImage.getComments()) {
			if (comment.getCommentStatusId() == COMMENT_STATUS_TYPE.ACTIVE.id()) {
				count++;
			}
		}
		log.debug("total comment count of the current image is:" + count);
		return count + "";
	}

	// generic confiuration for the photo gallery list
	public void loadBeanImageList() {
		if (activeBean.equals("dashBean")) {
			currentImageList = dashBean.getImageList();
		} else if (activeBean.equals("activityBean")) {
			currentImageList = activityBean.getImageList();
		}
	}

	public void setCurrentBeanListIndex(int index) {
		if (activeBean.equals("dashBean")) {
			dashBean.setCurrentImageIndex(index);
		} else if (activeBean.equals("activityBean")) {
			activityBean.setCurrentImageIndex(index);
		}
	}

	public Image getImageBasedOnIndex(int index) {
		if (activeBean.equals("dashBean")) {
			return dashBean.getImageList().get(index);
		} else if (activeBean.equals("activityBean")) {
			return activityBean.getImageList().get(index);
		}
		return null;
	}

	public void addTag() {
		if (activeBean.equals("dashBean")) {
			dashBean.addTag();
		} else if (activeBean.equals("activityBean")) {
			activityBean.addTag();
		}
	}

	public void doCommentFlag(long commentId) {
		if (activeBean.equals("dashBean")) {
			dashBean.setFlagReasonId(flagReasonId);
			dashBean.setFlagText(flagText);
			dashBean.flagComment(commentId);
		} else if (activeBean.equals("activityBean")) {
			activityBean.setFlagReasonId(flagCommentReasonId);
			activityBean.setFlagText(flagCommentText);
			activityBean.flagComment(commentId);
		}
	}

	
	public void doFlag() {
		if (activeBean.equals("dashBean")) {
			dashBean.setFlagReasonId(flagReasonId);
			dashBean.setFlagText(flagText);
			dashBean.flag();
		} else if (activeBean.equals("activityBean")) {
			activityBean.setFlagReasonId(flagReasonId);
			activityBean.setFlagText(flagText);
			activityBean.flag();
		}
	}

	public String printTaggedUser(long userId) {
		log.debug("User ID is:" + userId);
		if (currentIndex >= currentImageList.size()) {
			return null;
		}
		if (imageService.checkTagExistenceForUserToThisImage(userId,
				currentImageList.get(currentIndex).getImageId())) {
			log.debug("this user has already bean tagged with this image!");
			return "tagged";
		}
		log.debug("this user has not tagged with this image!");
		return "untagged";
	}
}
