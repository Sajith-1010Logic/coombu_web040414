package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.coombu.photobook.model.Image;
import com.coombu.photobook.service.IImageService;
import com.coombu.photobook.service.exception.DuplicateImageException;
import com.coombu.photobook.util.Constants.IMAGE_SOURCE;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;

@Named("fileUploadController")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FileUploadController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger log = LoggerFactory
			.getLogger(FileUploadController.class);

	private static final Integer MAX__NUM_UPLOAD_FILE = 5;

	@Autowired
	IImageService imageService;

	@Autowired
	DashBean dashBean;

	private List<Image> uploadedFiles;

	private String[] descr;

	@PostConstruct
	public void init() {
		uploadedFiles = new ArrayList<Image>(MAX__NUM_UPLOAD_FILE);
	}

	public List<Image> getUploadedFiles() {
		log.debug("getting uploaded files list - size:{}", uploadedFiles.size());
		return uploadedFiles;
	}

	public void setUploadedFiles(List<Image> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;

	}

	public String[] getDescr() {
		return descr;
	}

	public void setDescr(String[] descr) {
		this.descr = descr;
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			Image img = new Image();
			img.setUploadedFile(event.getFile());
			img.setEvent(dashBean.getCurrentEvent());
			img.setEventSecurityUser(dashBean.getCurrentEventSecurityUser());
			img.setImageSourceId(IMAGE_SOURCE.WEB.id());
			img.setUploadDateTime(new Date());
			img.setImageStatusTypeId(IMAGE_STATUS_TYPE.APPROVED.id());
			img.setOriginalFileName(event.getFile().getFileName());
			uploadedFiles.add(img);
			imageService.uploadPicture(img);
			FacesMessage msg = new FacesMessage("Succesful", event.getFile()
					.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			String msgStr = "Error uploading file "
					+ event.getFile().getFileName();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					msgStr, msgStr);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("ajaxStatusForUpload.hide()");
	}

	public void save() {
		try
		{
			imageService.save(uploadedFiles);
			uploadedFiles.clear();
			FacesMessage msg = new FacesMessage("Succesful: Files uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		catch(DuplicateImageException die)
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed: " + die.getMessage(),"Failed: " + die.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		catch(Exception e)
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Image saving failed.", "Image saving failed");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
		}
	}
	
	public String remove(Image image)
	{
		Iterator<Image> it = uploadedFiles.iterator();
		while(it.hasNext())
		{
			Image img = it.next();
			if ( img.getImageId() == image.getImageId() )
			{
				it.remove();
				break;
			}
		}
		return null;
	}
}
