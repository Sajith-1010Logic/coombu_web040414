package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.coombu.photobook.model.Image;
import com.coombu.photobook.util.Constants.SUBMENU_TYPE;

@Named("activityBean")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ActivityBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(DashBean.class);

	public String loadData(Long eventId, SUBMENU_TYPE type) {
		log.debug("entered loadData");
		if (!isEventIdAllowedAccess(eventId)) {
			handleError("Event does not exist or you are not authorized to view Event.");
			return "unauthorized.xhtml";
		}
		this.currentEventId = eventId;
		eventSecurityId = groupBean.getEventSecurityUserId(currentEventId);

		imageList = imageService.getEventImages(type, eventSecurityId,
				currentEventId);

		return "myactivity.xhtml";
	}

	public String getRowStart() {
		return "<!-- single row begin !-->\n"
				+ "<div style=\"width: 98%; height: 230px; padding-left: 8px;\">";
	}

	public String getRowEnd() {
		return "</div>\n" + "<div class=\"clear\"></div>\n"
				+ "<!-- single row !end-->";

	}

	public void setSelectedImageId(int selectedImageIndex, String dlg) {
		this.setCurrentImageIndex(selectedImageIndex);
		RequestContext context = RequestContext.getCurrentInstance();
		// context.execute("overlayWidget.hide()");
		if ("comment".equals(dlg)) {
			context.execute("wgtCommentDialog.show()");
			context.execute("wgtTagDialog.hide()");
			context.execute("wgtFlagDialog.hide()");
		} else if ("tag".equals(dlg)) {
			getTaggedUserIds();
			context.execute("wgtCommentDialog.hide()");
			context.execute("wgtFlagDialog.hide()");
			context.execute("wgtTagDialog.show()");
		} else if ("flag".equals(dlg)) {
			context.execute("wgtCommentDialog.hide()");
			context.execute("wgtTagDialog.hide()");
			context.execute("wgtFlagDialog.show()");
		}

	}

	public String deleteTheCurrentImage() {

		log.debug("activity image delete is called! current image index is:"
				+ currentImageIndex);
		Image currentImage = imageList.get(currentImageIndex);

		int res = imageService.deleteImage(currentImage.getImageId(),
				getEventSecurityId());
		if (res == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"You have deleted the current Image!", ""));

			Iterator it = imageList.iterator();
			while (it.hasNext()) {
				Image img = (Image) it.next();
				if (img.getImageId() == currentImage.getImageId()) {
					imageList.remove(img);
					log.debug("image removed!");
					break;
				}
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
		return null;
	}

	public String loadTheImage(long imageId, int index) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("componentId", imageId + "");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("currentIndex", index + "");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("activeBean", "activityBean");

		setCurrentImageIndex(index);

		log.debug("redirect the seperate page to load the current image!");

		return "dashBoardImageParent.xhtml";
	}
}
