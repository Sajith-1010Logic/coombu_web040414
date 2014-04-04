package com.coombu.photobook.presentation;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UICommand;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.coombu.photobook.dto.FlaggedCommentDTO;
import com.coombu.photobook.dto.FlaggedImageDTO;
import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.RemovalRequest;
import com.coombu.photobook.service.IAdminService;
import com.coombu.photobook.service.IImageService;
import com.coombu.photobook.service.IReferenceData;
import com.coombu.photobook.service.ReferenceData;
import com.coombu.photobook.service.exception.CoombuServiceException;
import com.coombu.photobook.util.Constants.COMMENT_STATUS_TYPE;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;
import com.coombu.photobook.util.Constants.REQUEST_REMOVAL_TYPE;

/**
 * 
 * @author sajith@1010logic.com Created on Feb 14, 2014
 * 
 *         Dash board Action for loading images posted by users also to repost
 *         or delete images
 * 
 */

@Named("adminBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AdminBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private IAdminService imageAdminService;

	@Autowired
	private IImageService imageService;

	@Autowired
	private DashBean dashBean;
	
	@Autowired
	public GroupBean groupBean;
	
	@Autowired
	public IReferenceData referenceData;

	private List<FlaggedImageDTO> flagedImageList;

	private List<FlaggedCommentDTO> flagedCommentList;

	private Comment comment;

	private Profile userProfile;

	private List<RemovalRequest> imageRemovalRequest;

	private List<Comment> commentRemovalRequest;

	private IMAGE_STATUS_TYPE currentType;

	private long currentEventId;

	private String currentImageUri;

	private int currentImageIndex;
	
	private List<RemovalRequest> rrList;

	private int indx = 0;

	public String getCurrentImageUri() {
		return currentImageUri;
	}

	public void setCurrentImageUri(String currentImageUri) {
		this.currentImageUri = currentImageUri;
	}

	public int getCurrentImageIndex() {
		return currentImageIndex;
	}

	public void setCurrentImageIndex(int currentImageIndex) {
		this.currentImageIndex = currentImageIndex;
	}

	private int firstRow = 0;
	private int rowsPerPage = 100;
	private int totalPages = 0;
	private int pageRange = 10;
	private Integer[] pages = null;
	private int currentPage = 0;
	private int totalRows = 0;

	private Logger log = LoggerFactory.getLogger("AdminBean.class");

	@PostConstruct
	public void init() {
		log.debug("entered admin loadData");
		if (!(dashBean.isEventIdAllowedAccess(dashBean.getCurrentEventId()))) {

		}
		currentEventId = dashBean.getCurrentEventId();

		currentType = IMAGE_STATUS_TYPE.ALL;
		firstRow = 0;
		totalPages = 0;
		currentPage = 0;
		totalRows = imageAdminService.getImagesCount(currentEventId);
		loadImageDataList();
	}

	/**
	 * 
	 * @param eventId
	 * @param type
	 * @return
	 */

	public String loadImageData() {

		return "admindash.xhtml";

	}

	private void loadImageDataList() {
		// Load list and totalCount.
		
		long securityUserId = groupBean.getEventSecurityUserId(currentEventId);
		rrList = imageAdminService.getCommentRemovalRequest(currentEventId);
		setCommentRemovalRequest(imageAdminService.getFlaggedComments(currentEventId, securityUserId));
		setImageRemovalRequest  (imageAdminService.getImageRemovalRequest(currentEventId));		
		getCurrentImageDetails();
		getRemovalComment();
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
		loadImageDataList(); // Load requested page.
	}

	public String getNextImageUri() {
		StringBuffer uri = new StringBuffer("/image?id=");
		if (currentImageIndex >= 3) {
			pageNext();
			currentImageIndex = 0;
		} else {
			currentImageIndex++;
		}
		if (currentImageIndex >= this.flagedImageList.size())
			currentImageIndex = flagedImageList.size() - 1;
		uri.append(this.flagedImageList.get(currentImageIndex).getImage()
				.getFileName());
		currentImageUri = uri.toString();
		return currentImageUri;
	}

	public String getPrevImageUri() {
		StringBuffer uri = new StringBuffer("/image?id=");
		if (currentImageIndex <= 0) {
			pagePrevious();
			currentImageIndex = 3;
		} else {
			currentImageIndex--;
		}
		if (currentImageIndex < 0)
			currentImageIndex = 0;
		uri.append(this.flagedImageList.get(currentImageIndex).getImage()
				.getFileName());
		currentImageUri = uri.toString();
		return currentImageUri;
	}

	public void currentImageIndex(ActionEvent event) {
		this.currentImageIndex = (Integer) event.getComponent().getAttributes()
				.get("curId");
	}

	public int getIndex() {
		return ++indx;
	}

	/**
	 * @param selectedImageIndex
	 *            -imageId to be deleted or repost
	 * @param dlg
	 */

	public void setSelectedImageId(int selectedImageIndex, String dlg) {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("overlayWidget.hide()");

		if ("delete".equalsIgnoreCase(dlg)) {
			imageAdminService.deleteImage((long) selectedImageIndex);

			/*Iterator it = getFlagedImageList().iterator();
			while (it.hasNext()) {
				FlaggedImageDTO img = (FlaggedImageDTO) it.next();
				if (img.getImage().getImageId() == selectedImageIndex) {
					getFlagedImageList().remove(img);
					log.debug("image is removed from flag report!");
				}
			}*/

		} else if ("repost".equalsIgnoreCase(dlg)) {
			imageAdminService.repostImage((long) selectedImageIndex);

			/*Iterator it = getFlagedImageList().iterator();
			while (it.hasNext()) {
				FlaggedImageDTO img = (FlaggedImageDTO) it.next();
				if (img.getImage().getImageId() == selectedImageIndex) {
					getFlagedImageList().remove(img);
					log.debug("image is removed from flag report!");
				}
			}*/

		}
		
		String url = ("admindash.xhtml");
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		try {
	        ec.redirect(url);
	} catch (IOException ex) {
	       log.debug("check redirect"+url);
	}

	}

	public void setSelectedComment(long removalRequestId, int selectedCommentIndex, String dlg)
			throws CoombuServiceException {
		RequestContext context = RequestContext.getCurrentInstance();

		context.execute("overlayWidget.hide()");

		if ("delete".equalsIgnoreCase(dlg)) {
			imageService.repostComment(selectedCommentIndex, removalRequestId,
					COMMENT_STATUS_TYPE.INACTIVE.id());
		
		} else if ("repost".equalsIgnoreCase(dlg)) {
			imageService.repostComment(selectedCommentIndex, removalRequestId,
					COMMENT_STATUS_TYPE.ACTIVE.id());
			
			
		}
		
		String url = ("admindash.xhtml");
				FacesContext fc = FacesContext.getCurrentInstance();
				ExternalContext ec = fc.getExternalContext();
				try {
			        ec.redirect(url);
			} catch (IOException ex) {
			       log.debug("check redirect"+url);
			}
	}

	public void getCurrentImageDetails() {
		if (getImageRemovalRequest() == null || getImageRemovalRequest().size() <= 0) 
			return;
		List<FlaggedImageDTO> imageDetails = new ArrayList<FlaggedImageDTO>();
		for (RemovalRequest rrquest : getImageRemovalRequest())
		{
			FlaggedImageDTO flagedDto = new FlaggedImageDTO();
			Image img = imageService.getImages(rrquest.getImageCommentId());
			flagedDto.setImage(img);
			flagedDto.setUserProfile(rrquest.getUserProfile());
			flagedDto.setFlagedBy(rrquest.getUserProfile());
			flagedDto.setReason(referenceData.getReason(rrquest.getRequestReasonId()));
			flagedDto.setDescription(rrquest.getRequestorComment());
			imageDetails.add(flagedDto);
		}
		setFlagedImageList(imageDetails);

	}

	public void getRemovalComment() 
	{
		flagedCommentList = new ArrayList<FlaggedCommentDTO>();
		if(getCommentRemovalRequest() == null	|| getCommentRemovalRequest().size() <= 0)
			return;		

		for (Comment rrquest : getCommentRemovalRequest()) {
			FlaggedCommentDTO flagedDto = new FlaggedCommentDTO();			
			flagedDto.setComment(rrquest);
			flagedDto.setUserProfile(rrquest.getEventSecurityUser().getSecurityUser().getUserProfile());
			flagedDto.setFileName(rrquest.getImage().getFileName());
			for(RemovalRequest rr : rrList)
			{
				if ( rr.getRemovalRequestTypeId() == REQUEST_REMOVAL_TYPE.COMMENT.id())
				{
					flagedDto.setRemovalRequestId(rr.getRemovalRequestId());
					flagedDto.setFlagedBy(rr.getUserProfile());
					flagedDto.setDescription(rr.getRequestorComment());
					flagedDto.setReason(referenceData.getReason(rr.getRequestReasonId()));
					break;
				}
			}
			flagedCommentList.add(flagedDto);
		}
	}

	public IAdminService getImageAdminService() {
		return imageAdminService;
	}

	public void setImageAdminService(IAdminService imageAdminService) {
		this.imageAdminService = imageAdminService;
	}

	public IMAGE_STATUS_TYPE getCurrentType() {
		return currentType;
	}

	public void setCurrentType(IMAGE_STATUS_TYPE currentType) {
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

	public long getCurrentEventId() {
		return currentEventId;
	}

	public void setCurrentEventId(long currentEventId) {
		this.currentEventId = currentEventId;
	}

	public DashBean getDashBean() {
		return dashBean;
	}

	public void setDashBean(DashBean dashBean) {
		this.dashBean = dashBean;
	}

	public List<RemovalRequest> getImageRemovalRequest() {
		return imageRemovalRequest;
	}

	public void setImageRemovalRequest(List<RemovalRequest> imageRemovalRequest) {
		this.imageRemovalRequest = imageRemovalRequest;
	}

	
	public List<Comment> getCommentRemovalRequest() {
		return commentRemovalRequest;
	}

	public void setCommentRemovalRequest(List<Comment> commentRemovalRequest) {
		this.commentRemovalRequest = commentRemovalRequest;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public Profile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(Profile userProfile) {
		this.userProfile = userProfile;
	}

	public IImageService getImageService() {
		return imageService;
	}

	public void setImageService(IImageService imageService) {
		this.imageService = imageService;
	}

	public List<FlaggedImageDTO> getFlagedImageList() {
		return flagedImageList;
	}

	public void setFlagedImageList(List<FlaggedImageDTO> flagedImageList) {
		this.flagedImageList = flagedImageList;
	}

	public List<FlaggedCommentDTO> getFlagedCommentList() {
		return flagedCommentList;
	}

	public void setFlagedCommentList(List<FlaggedCommentDTO> flagedCommentList) {
		this.flagedCommentList = flagedCommentList;
	}

	public GroupBean getGroupBean() {
		return groupBean;
	}

	public void setGroupBean(GroupBean groupBean) {
		this.groupBean = groupBean;
	}
	/**
	 * @return the referenceData
	 */
	public IReferenceData getReferenceData() {
		return referenceData;
	}

	/**
	 * @param referenceData the referenceData to set
	 */
	public void setReferenceData(IReferenceData referenceData) {
		this.referenceData = referenceData;
	}
}
