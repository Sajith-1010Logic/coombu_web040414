package com.coombu.photobook.presentation;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.coombu.photobook.model.Image;
import com.coombu.photobook.service.IImageService;

@Named("showActivity")
@Scope(value = "view")
public class ShowActivityComponentBean {

	Map<String, String> params = null;

	private Logger log = LoggerFactory
			.getLogger(ShowActivityComponentBean.class);

	private long currentImageId;

	private Image image;

	@Autowired
	private IImageService imageService;

	public long getCurrentImageId() {
		return currentImageId;
	}

	public void setCurrentImageId(long currentImageId) {
		this.currentImageId = currentImageId;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public IImageService getImageService() {
		return imageService;
	}

	public void setImageService(IImageService imageService) {
		this.imageService = imageService;
	}

	@PostConstruct
	public void init() {
		params = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		currentImageId = Long.parseLong(params.get("componentId"));
		log.debug("the active component id is:" + currentImageId);

		image = imageService.getImages(currentImageId);

	}

}
