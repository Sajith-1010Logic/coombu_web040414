package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.Event;
import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageVote;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;
import com.coombu.photobook.util.Constants.SUBMENU_TYPE;

@Named("dashBean")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DashBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(DashBean.class);

	private List<ImageVote> activityList;

	private List<ImageVote> myActivityList;

	private List<ImageVote> groupActivityList;

	private SUBMENU_TYPE currentType;

	// private List<EventSecurityUser> selectedUserIds;

	private int firstRow = 0;
	private int rowsPerPage = 10;
	private int totalPages = 0;
	private int pageRange = 10;
	private Integer[] pages = null;
	private int currentPage = 0;
	private int totalRows = 0;

	private long selectedImageId;

	private String currentImageUri;
	// private int currentImageIndex; //this is set in parent class

	private long imageIdforSeperatePage;

	private int myCommentCount;

	private int myImageCount;

	private List<EventSecurityUser> users;

	/*
	 * public List<EventSecurityUser> getSelectedUserIds() { return
	 * selectedUserIds; }
	 * 
	 * public void setSelectedUserIds(List<EventSecurityUser> selectedUserIds) {
	 * this.selectedUserIds = selectedUserIds; }
	 */
	public String loadData(Long eventId, SUBMENU_TYPE type) {
		log.debug("entered loadData");
		if (!isEventIdAllowedAccess(eventId)) {
			handleError("Event does not exist or you are not authorized to view Event.");
			return "unauthorized.xhtml";
		}
		currentEventId = eventId;
		currentType = type;
		firstRow = 0;
		totalPages = 0;
		currentPage = 0;
		eventSecurityId = groupBean.getEventSecurityUserId(currentEventId);
		totalRows = imageService.getEventImagesCount(currentType,
				currentEventId);
		loadDataList();
		activityList = imageService.getImageVoteByEvent(eventId);
		setMyImageCount(imageService.getImageCount(eventSecurityId,
				currentEventId));
		setMyCommentCount(imageService.getCommentCount(eventSecurityId,
				currentEventId));
		setActivityTypes(activityList);
		log.debug("exited loadData");
		return "dash.xhtml";

	}

	public long getImageIdforSeperatePage() {
		return imageIdforSeperatePage;
	}

	public void setImageIdforSeperatePage(long imageIdforSeperatePage) {
		this.imageIdforSeperatePage = imageIdforSeperatePage;
	}

	private void setActivityTypes(List<ImageVote> activityList2) {
		myActivityList = new ArrayList<ImageVote>();
		groupActivityList = new ArrayList<ImageVote>();
		for (ImageVote vote : activityList2) {
			if (vote.getEventSecurityUser().getEventSecurityUserId() == this.eventSecurityId) {
				myActivityList.add(vote);
			} else {
				groupActivityList.add(vote);
			}

		}

	}

	public Event getCurrentEvent() {
		Event currentEvent = null;
		for (EventSecurityUser seu : this.groupBean.getUserEvents()) {
			if (seu.getEvent().getEventId() == this.currentEventId) {
				currentEvent = seu.getEvent();
				break;
			}
		}
		return currentEvent;
	}

	public List<Image> getImageList() {
		return imageList;
	}

	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}

	public SUBMENU_TYPE getCurrentType() {
		return currentType;
	}

	public void setCurrentType(SUBMENU_TYPE currentType) {
		this.currentType = currentType;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageRange() {
		return pageRange;
	}

	public void setPageRange(int pageRange) {
		this.pageRange = pageRange;
	}

	public Integer[] getPages() {
		return pages;
	}

	public void setPages(Integer[] pages) {
		this.pages = pages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	// Loaders
	// ------------------------------------------------------------------------------------

	public String getCurrentImageUri() {
		return currentImageUri;
	}

	public void setCurrentImageUri(String currentImageUri) {
		this.currentImageUri = currentImageUri;
	}

	public Image getCurrentImage() {
		if (currentImageIndex >= 0 && !imageList.isEmpty()) {
			return imageList.get(currentImageIndex);
		}
		return null;
	}

	private void loadDataList() {

		// Load list and totalCount.
		setImageList(imageService.getEventImages(currentType, currentEventId,
				firstRow, rowsPerPage));

		// Set currentPage, totalPages and pages.
		currentPage = (totalRows / rowsPerPage)
				- ((totalRows - firstRow) / rowsPerPage) + 1;
		totalPages = (totalRows / rowsPerPage)
				+ ((totalRows % rowsPerPage != 0) ? 1 : 0);
		int pagesLength = Math.min(pageRange, totalPages);
		pages = new Integer[pagesLength];

		// firstPage must be greater than 0 and lesser than
		// totalPages-pageLength.
		int firstPage = Math.min(Math.max(0, currentPage - (pageRange / 2)),
				totalPages - pagesLength);

		// Create pages (page numbers for page links).
		for (int i = 0; i < pagesLength; i++) {
			pages[i] = ++firstPage;
		}
	}

	// Paging actions
	// -----------------------------------------------------------------------------

	public void pageFirst() {
		page(0);
	}

	public void pageNext() {
		page(firstRow + rowsPerPage);
	}

	public void pagePrevious() {
		page(firstRow - rowsPerPage);
	}

	public void pageLast() {
		page(totalRows
				- ((totalRows % rowsPerPage != 0) ? totalRows % rowsPerPage
						: rowsPerPage));
	}

	public void page(ActionEvent event) {
		page(((Integer) ((UICommand) event.getComponent()).getValue() - 1)
				* rowsPerPage);
	}

	private void page(int firstRow) {
		this.firstRow = firstRow;
		loadDataList(); // Load requested page.
	}

	/*
	 * public String getNextImageUri() { StringBuffer uri = new
	 * StringBuffer("/image?type=lightbox&id="); if (currentImageIndex >= 3) {
	 * pageNext(); currentImageIndex = 0; } else { currentImageIndex++; } if
	 * (currentImageIndex >= this.imageList.size()) currentImageIndex =
	 * imageList.size() - 1;
	 * 
	 * if (currentImageIndex >= 0) {
	 * uri.append(this.imageList.get(currentImageIndex).getFileName());
	 * currentImageUri = uri.toString(); } return currentImageUri; }
	 * 
	 * public String getPrevImageUri() { StringBuffer uri = new
	 * StringBuffer("/image?type=lightbox&id="); if (currentImageIndex <= 0) {
	 * pagePrevious(); currentImageIndex = rowsPerPage - 1; } else {
	 * currentImageIndex--; } if (currentImageIndex < 0) currentImageIndex = 0;
	 * 
	 * uri.append(this.imageList.get(currentImageIndex).getFileName());
	 * currentImageUri = uri.toString(); return currentImageUri; }
	 */

	public int getCurrentImageIndex() {
		return currentImageIndex;
	}

	public void setCurrentImageIndex(int currentImageIndex) {
		this.currentImageIndex = currentImageIndex;
		StringBuffer uri = new StringBuffer("/image?type=lightbox&id=");
		uri.append(this.imageList.get(currentImageIndex).getFileName());
		this.currentImageUri = uri.toString();
		/*
		 * RequestContext context = RequestContext.getCurrentInstance();
		 * context.update("popup"); context.execute("overlayWidget.show()");
		 */
	}

	public void currentImageIndex(ActionEvent event) {
		this.currentImageIndex = (Integer) event.getComponent().getAttributes()
				.get("curId");
	}

	public int getIndex() {
		return ++indx;
	}

	public List<ImageVote> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<ImageVote> activityList) {
		this.activityList = activityList;
	}

	public List<ImageVote> getMyActivityList() {
		return myActivityList;
	}

	public void setMyActivityList(List<ImageVote> myActivityList) {
		this.myActivityList = myActivityList;
	}

	public List<ImageVote> getGroupActivityList() {
		return groupActivityList;
	}

	public void setGroupActivityList(List<ImageVote> groupActivityList) {
		this.groupActivityList = groupActivityList;
	}

	public Set<Comment> getCurrentImageActivity() {
		Set<Comment> commentSet = null;
		if (imageList == null)
			return new HashSet<Comment>();
		if (currentImageIndex >= imageList.size())
			currentImageIndex = imageList.size() - 1;
		if (currentImageIndex < 0)
			currentImageIndex = 0;
		if (imageList.size() <= 0)
			return new HashSet<Comment>();
		Image img = imageList.get(currentImageIndex);
		img = imageService.getImage(img.getImageId(), true);
		return img.getComments();
	}

	public int getMyCommentCount() {
		return myCommentCount;
	}

	public void setMyCommentCount(int myCommentCount) {
		this.myCommentCount = myCommentCount;
	}

	public int getMyImageCount() {
		return myImageCount;
	}

	public void setMyImageCount(int myImageCount) {
		this.myImageCount = myImageCount;
	}

	private int indx = 0;

	public void setSelectedImageId(int selectedImageIndex, String dlg) {
		this.currentImageIndex = selectedImageIndex;
		RequestContext context = RequestContext.getCurrentInstance();
		if ("comment".equals(dlg)) {
			context.execute("wgtCommentDialog.show()");
			context.execute("wgtTagDialog.hide()");
			context.execute("wgtFlagDialog.hide()");

		} else if ("tag".equals(dlg)) {
			context.execute("wgtCommentDialog.hide()");
			context.execute("wgtFlagDialog.hide()");
			context.execute("wgtTagDialog.show()");

		} else if ("flag".equals(dlg)) {
			context.execute("wgtCommentDialog.hide()");
			context.execute("wgtTagDialog.hide()");
			context.execute("wgtFlagDialog.show()");

		} else if ("delete".equals(dlg)) {
			context.execute("wgtCommentDialog.hide()");
			context.execute("wgtTagDialog.hide()");
			context.execute("wgtFlagDialog.hide()");

		}
	}

	public String toggleLike(long imageId) {
		addLike(imageId);
		log.debug("Add like is called!");

		return null;
	}

	public boolean checkImageLikeExistence(long imageId) {

		if (imageService.checkImageLikeExistence(imageId, currentEventId,
				eventSecurityId)) {
			return true;
		}
		return false;

	}

	public void addLike(long imageId) {
		log.debug("like is called!");
		super.addLike(imageId);
		/*
		 * RequestContext context = RequestContext.getCurrentInstance();
		 * context.execute("overlayWidget.hide()");
		 */
	}

	public void unLike(long imageId) {
		super.unLike(imageId);
		/*
		 * RequestContext context = RequestContext.getCurrentInstance();
		 * context.execute("overlayWidget.hide()");
		 */
	}

	/*
	 * public void addTag() { RequestContext context =
	 * RequestContext.getCurrentInstance();
	 * 
	 * context.execute("overlayWidget.hide()"); }
	 */

	public void flag() {
		log.debug("Flagging is called!");
		super.flag();
		/*
		 * RequestContext context = RequestContext.getCurrentInstance();
		 * context.execute("overlayWidget.hide()");
		 */

	}

	public void deleteImage() {
		int d = super.delete(currentImageIndex);
		log.debug("------------ delete images--------" + d);
		/*
		 * RequestContext context = RequestContext.getCurrentInstance();
		 * context.execute("overlayWidget.hide()");
		 */
	}

	public List<EventSecurityUser> getCurrentEventUsers() {
		if (users == null) {
			users = userService.getEventSecurityUsersByEventId(currentEventId,
					true, false, false, false, false);
		}
		return users;
	}

	public UserProfile getUserProfile() {
		return getCurrentEventSecurityUser().getSecurityUser().getUserProfile();
	}

	public String addNewComment() {
		log.debug("Adding a new comment!");
		super.addComment();
		return null;
	}

	public boolean checkCommentExistence(long imageId) {

		if (imageService.checkCommentExistence(imageId, getEventSecurityId())) {
			log.debug("You have already added a comment for this picture!");
			return true;
		}
		log.debug("there are no comments available for this picture!");
		return false;
	}

	public boolean checkTagExistence(long imageId) {

		if (imageService.checkTagExistence(imageId,
				getCurrentEventSecurityUser().getSecurityUser()
						.getSecurityUserId())) {
			log.debug("You have already tagged this image!");
			return true;
		}
		log.debug("you did't tagged this image!");
		return false;
	}

	public boolean checkVisibleStatus(Image img) {
		if (img.getImageStatusTypeId() == IMAGE_STATUS_TYPE.APPROVED.id()) {
			return true;
		}
		return false;
	}

	public String loadTheImage(long imageId, int index) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("componentId", imageId + "");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("currentIndex", index + "");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("activeBean", "dashBean");

		setCurrentImageIndex(index);

		log.debug("redirect the seperate page to load the current image!");

		return "dashBoardImageParent.xhtml";
	}

	public String getCommentCount(Image img) {
		int count = imageService.getCommentCountByImage(img.getImageId());
		log.debug("total comment count of the image is: " + count);
		return count + "";
	}

	public String printTaggedUser(long userId) {
		log.debug("User ID is:" + userId);
		if (currentImageIndex >= imageList.size()) {
			return null;
		}
		if (imageService.checkTagExistenceForUserToThisImage(userId, imageList
				.get(currentImageIndex).getImageId())) {
			log.debug("this user has already bean tagged with this image!");
			return "tagged";
		}
		log.debug("this user has not tagged with this image!");
		return "untagged";
	}

	public boolean isUserAuthenticatedToTag() {
		if (currentImageIndex >= imageList.size()) {
			return false;
		}
		if (imageList.get(currentImageIndex).getEventSecurityUser()
				.getSecurityUser().getUserProfile().getUserProfileId() == getUserProfile()
				.getUserProfileId()) {
			return true;
		}
		return false;
	}
}
