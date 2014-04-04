package com.coombu.photobook.presentation;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.coombu.photobook.dto.PhotoBookDTO;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageBucket;
import com.coombu.photobook.model.ImageBucketImage;
import com.coombu.photobook.service.IImageService;
import com.coombu.photobook.service.IMailService;
import com.coombu.photobook.service.IPhotoBookSevice;
import com.coombu.photobook.util.Constants.IMAGE_BUCKET_STATUS_TYPE;

@Named("photoBookBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PhotoBookBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(PhotoBookBean.class);

	@Autowired
	private IImageService imageService;

	@Autowired
	private DashBean dashBean;

	@Autowired
	private IPhotoBookSevice photoBookService;

	@Autowired
	IMailService emailService;

	private List<Image> imageList;

	private List<Integer> selectedListIndex = null;

	private String albumName;

	private String albumDescription;

	private int currentEventId;

	private ImageBucket imageBucket;

	private String status = "create";

	private List<ImageBucketImage> albumImage;

	private List<ImageBucketImage> selectedImages;

	private List<Integer> imageids;

	List<Integer> removedImageId;

	@PostConstruct
	public void init() {
		selectedListIndex = new ArrayList<Integer>();
		removedImageId = new ArrayList<Integer>();
		imageids = new ArrayList<Integer>();
		imageList = imageService.getEventImages(dashBean.getCurrentType(),
				dashBean.getCurrentEventId());
		log.debug("List of Images :" + imageList.size());
		String eventId = String.valueOf(dashBean.getCurrentEventId());
		currentEventId = Integer.valueOf(eventId);
		List<ImageBucket> imgBucket = photoBookService
				.getPhotoBook(currentEventId);
		if (!(imgBucket != null && imgBucket.isEmpty())) {
			imageBucket = imgBucket.get(0);
		}

		selectedImages = photoBookService
				.getPartialySavedImages(currentEventId);
		// log.debug("-------save photo book list-------------"+selectedImages.size());
		albumImage = photoBookService.getAlbumImages(currentEventId,
				IMAGE_BUCKET_STATUS_TYPE.PUBLISH.id());
		log.debug("Photo Book management init is called!");

	}

	public String loadData() {
		if (imageBucket != null) {
			if (imageBucket.getImageStatusType() == IMAGE_BUCKET_STATUS_TYPE.PARTIALSAVE
					.id())
				return "modify_photobook.xhtml";
			else if (imageBucket.getImageStatusType() == IMAGE_BUCKET_STATUS_TYPE.PUBLISH
					.id())
				return "viewphotobook.xhtml";
		}

		return "photobook.xhtml";

	}

	public String modifyData() {

		return "modify_photobook.xhtml";
	}

	public String viewData() {
		log.debug("----------View photo book---------");
		return "viewphotobook.xhtml";
	}

	public void setSelectedImageId(Integer imageId) {
		log.debug("------ImageId----------" + imageId);
		if (!selectedListIndex.contains(imageId)) {
			log.debug("Added");
			selectedListIndex.add(imageId);
		} else {
			log.debug("removed");
			selectedListIndex.remove(imageId);
		}
		log.debug("-------listsize------" + selectedListIndex.size());
	}

	public void publishPhotoBook(ActionEvent event) {
		FacesMessage msg = null;
		RequestContext context = RequestContext.getCurrentInstance();
		log.debug("entered photbook publish function");

		log.debug("----getalbumname---------" + getAlbumName());
		if (getAlbumName() == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Enter Album Name", "");
		} else if (getAlbumDescription() == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Enter Album Description", "");
		} else if (selectedListIndex.size() <= 0) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Please select Images ", "");
		}
		log.debug("----getdescription--------" + getAlbumDescription());
		List<ImageBucket> imgBucket = photoBookService
				.getPhotoBook(currentEventId);
		if (imgBucket.size() > 0) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Already one photobook created ", "");
			context.execute("saveButton.hide()");
		} else {
			context.execute("saveButton.show()");
		}

		PhotoBookDTO photoDto = new PhotoBookDTO();
		photoDto.setAlbumName(getAlbumName());
		photoDto.setAlbumDescription(getAlbumDescription());
		photoDto.setImages(selectedListIndex);
		photoDto.setEventId(currentEventId);
		if (msg != null) {
			FacesContext.getCurrentInstance().addMessage(null, msg);
			resetCreateForm();

		}

		else {
			if (photoBookService.SavePhotoBook(photoDto,
					IMAGE_BUCKET_STATUS_TYPE.PUBLISH.id()) == 0) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Cannot able to save Data", ""));
			} else {

				String emailaddress = dashBean.getCurrentEventSecurityUser()
						.getSecurityUser().getUserProfile().getEmailAddress();
				String emailContent = "Hi "
						+ dashBean.getCurrentEventSecurityUser()
								.getSecurityUser().getUserProfile()
								.getFirstName()
						+ "  "
						+ dashBean.getCurrentEventSecurityUser()
								.getSecurityUser().getUserProfile()
								.getLastName()

						+ "\n\n"
						+ "A new Photo book "
						+ getAlbumName()
						+ " has been Created. "

						+ "\n\n"
						+ "If you have any questions, please feel free to send an email to support@coombu.com"
						+ "\n\n" + "Have fun!" + "\n\n" + "Cheers," + "Coombu"
						+ "\n";

				// sending email
				emailService.sendEmail("coombu@caseopinion.com",
						"Photo Book Created", emailContent, emailaddress);
				emailService.sendEmail("coombu@caseopinion.com",
						"Photo Book Created", emailContent, "admin@coombu.com");
				selectedListIndex.clear();
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Photo Book Published", ""));
				resetViewForm();

			}
		}
	}

	public void savePhotoBook(ActionEvent event) {
		FacesMessage msg = null;
		RequestContext context = RequestContext.getCurrentInstance();
		log.debug("entered photbook save function");

		log.debug("----getalbumname---------" + getAlbumName());
		if (getAlbumName() == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Enter Album Name", "");
		} else if (getAlbumDescription() == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Enter Album Description", "");
		} else if (selectedListIndex.size() <= 0) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Please select Images ", "");
		}
		log.debug("----getdescription--------" + getAlbumDescription());
		List<ImageBucket> imgBucket = photoBookService
				.getPhotoBook(currentEventId);
		if (imgBucket.size() > 0) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Already one photobook created ", "");
			context.execute("saveButton.hide()");
		} else {
			context.execute("saveButton.show()");
		}

		PhotoBookDTO photoDto = new PhotoBookDTO();
		photoDto.setAlbumName(getAlbumName());
		photoDto.setAlbumDescription(getAlbumDescription());
		photoDto.setImages(selectedListIndex);
		photoDto.setEventId(currentEventId);
		if (msg != null) {
			FacesContext.getCurrentInstance().addMessage(null, msg);
			resetCreateForm();
		}

		else {
			if (photoBookService.SavePhotoBook(photoDto,
					IMAGE_BUCKET_STATUS_TYPE.PARTIALSAVE.id()) == 0) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Cannot able to save Data", ""));
			} else {

				selectedListIndex.clear();
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Photo Book saved", ""));
				resetModifyForm();
			}
		}
	}

	public void updatePhotoBook(ActionEvent event) {
		FacesMessage msg = null;
		RequestContext context = RequestContext.getCurrentInstance();

		if (imageBucket.getImageBucketName() == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Enter Album Name", "");
		} else if (imageBucket.getImageBucketDescription() == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Enter Album Description", "");
		} else if (imageList.size() > 0 && imageids.size() <= 0) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Please select Images ", "");
		}

		removeDuplicateValues();

		if (msg != null) {
			FacesContext.getCurrentInstance().addMessage(null, msg);
			resetModifyForm();
		}

		else if (photoBookService.modifyPhotoBook(imageBucket, imageids,
				IMAGE_BUCKET_STATUS_TYPE.PARTIALSAVE.id()) == 0) {

			log.debug("ERROR  BLOCK EXE");
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Cannot able to save Record", ""));
		} else {

			log.debug("MAIN  BLOCK EXE");

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Photo Book Updated", ""));
			resetModifyForm();
		}

	}

	public void publishModifiedPhotoBook(ActionEvent event) {
		FacesMessage msg = null;
		RequestContext context = RequestContext.getCurrentInstance();

		if (imageBucket.getImageBucketName() == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Enter Album Name", "");
		} else if (imageBucket.getImageBucketDescription() == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Enter Album Description", "");
		} else if (getSelectedImages().size() <= 0
				&& selectedListIndex.size() <= 0) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Please select Images ", "");
		}

		removeDuplicateValues();

		if (msg != null) {
			FacesContext.getCurrentInstance().addMessage(null, msg);
			resetModifyForm();
		}

		else

		if (photoBookService.modifyPhotoBook(imageBucket, imageids,
				IMAGE_BUCKET_STATUS_TYPE.PUBLISH.id()) == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Cannot able to save Record", ""));
		} else {

			String emailaddress = dashBean.getCurrentEventSecurityUser()
					.getSecurityUser().getUserProfile().getEmailAddress();
			String emailContent = "Hi "
					+ dashBean.getCurrentEventSecurityUser().getSecurityUser()
							.getUserProfile().getFirstName()
					+ "  "
					+ dashBean.getCurrentEventSecurityUser().getSecurityUser()
							.getUserProfile().getLastName()

					+ "\n\n"
					+ "A new Photo book "
					+ getAlbumName()
					+ " has been Created."

					+ "\n\n"
					+ "If you have any questions, please feel free to send an email to support@coombu.com"
					+ "\n\n" + "Have fun!" + "\n\n" + "Cheers," + "Coombu"
					+ "\n";

			// sending email
			emailService.sendEmail("coombu@caseopinion.com",
					"Photo Book Created", emailContent, "admin@coombu.com");
			selectedListIndex.clear();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Photo Book Published", ""));
			resetViewForm();

		}

	}

	public void resetCreateForm() {
		loadData();
		String url = ("photobook.xhtml");
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		ec.getFlash().setKeepMessages(true);
		try {
			ec.redirect(url);
		} catch (IOException ex) {
			log.debug("check redirect" + url);
		}
	}

	public void resetModifyForm() {
		modifyData();
		String url = ("modify_photobook.xhtml");
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		ec.getFlash().setKeepMessages(true);
		try {
			ec.redirect(url);
		} catch (IOException ex) {
			log.debug("check redirect" + url);
		}
	}

	public boolean checkImageIsAddedCart(Integer imageId) {

		if (selectedListIndex.contains(imageId)) {
			return true;
		} else {

			return false;
		}

	}

	public boolean checkImageAlreadyInBucket(Integer id) {

		log.debug("----Rendered------");

		for (ImageBucketImage i : selectedImages) {
			Integer temp = (int) i.getImageName().getImageId();
			if (!imageids.contains(temp)) {
				imageids.add(temp);
			}
		}
		if (imageids.contains(id)) {

			return true;
		} else {
			return false;
		}

	}

	public void modifyImageBucket(Integer imageId) {

		log.debug("Modify Called");
		log.debug("ImageId" + imageId);
		log.debug("ArrayList Size" + imageids.size());
		if (imageids.contains(imageId)) {
			imageids.remove(imageId);
			if (!removedImageId.contains(imageId)) {
				removedImageId.add(imageId);
				photoBookService
						.removeUnCheckedImagesFromBucket(removedImageId);
				selectedImages = photoBookService
						.getPartialySavedImages(currentEventId);

			}
			log.debug("ArrayList Size Removed" + imageids.size());
		} else {

			imageids.add(imageId);
			log.debug("ADDED" + imageId);
			log.debug("ArrayList Size Added" + imageids.size());
		}

	}

	public void resetViewForm() {
		modifyData();
		String url = ("viewphotobook.xhtml");
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		ec.getFlash().setKeepMessages(true);
		try {
			ec.redirect(url);
		} catch (IOException ex) {
			log.debug("check redirect" + url);
		}
	}

	public void removeDuplicateValues() {

		selectedImages = photoBookService
				.getPartialySavedImages(currentEventId);
		for (ImageBucketImage i : selectedImages) {
			Integer temp = (int) i.getImageName().getImageId();
			if (imageids.contains(temp)) {
				imageids.remove(temp);
			}
		}

	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumDescription() {
		return albumDescription;
	}

	public void setAlbumDescription(String albumDescription) {
		this.albumDescription = albumDescription;
	}

	public String getRowStart() {
		return "<!-- single row begin !-->\n"
				+ "<div style=\"width: 98%; height: 230px; padding-left: 8px;\">";
	}

	public String getRowEnd() {
		return "</div>\n" + "<div class=\"clear\"></div>\n"
				+ "<!-- single row !end-->";

	}

	public IImageService getImageService() {
		return imageService;
	}

	public void setImageService(IImageService imageService) {
		this.imageService = imageService;
	}

	public List<Image> getImageList() {
		return imageList;
	}

	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}

	public DashBean getDashBean() {
		return dashBean;
	}

	public void setDashBean(DashBean dashBean) {
		this.dashBean = dashBean;
	}

	public IPhotoBookSevice getPhotoBookService() {
		return photoBookService;
	}

	public void setPhotoBookService(IPhotoBookSevice photoBookService) {
		this.photoBookService = photoBookService;
	}

	public IMailService getEmailService() {
		return emailService;
	}

	public void setEmailService(IMailService emailService) {
		this.emailService = emailService;
	}

	public int getCurrentEventId() {
		return currentEventId;
	}

	public void setCurrentEventId(int currentEventId) {
		this.currentEventId = currentEventId;
	}

	public ImageBucket getImageBucket() {
		return imageBucket;
	}

	public void setImageBucket(ImageBucket imageBucket) {
		this.imageBucket = imageBucket;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ImageBucketImage> getSelectedImages() {
		return selectedImages;
	}

	public void setSelectedImages(List<ImageBucketImage> selectedImages) {
		this.selectedImages = selectedImages;
	}

	public List<ImageBucketImage> getAlbumImage() {
		return albumImage;
	}

	public void setAlbumImage(List<ImageBucketImage> albumImage) {
		this.albumImage = albumImage;
	}

	public List<Integer> getSelectedListIndex() {
		return selectedListIndex;
	}

	public void setSelectedListIndex(List<Integer> selectedListIndex) {
		this.selectedListIndex = selectedListIndex;
	}

}
