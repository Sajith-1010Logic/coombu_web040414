package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.coombu.photobook.model.Schedule;
import com.coombu.photobook.service.IScheduleService;

@Named("schedule")
@Scope(value = "view")
public class ScheduleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pictureAvailableStartDate;

	private String pictureAvailableEndDate;

	private String uploadImageStartDate;

	private String uploadImageEndDate;

	private String bookStartDate;

	private String bookEndDate;

	private Schedule schedule;

	private String uploadPicutureScheduleMsg;

	private String bookScheduleMsg;

	private String pictureAvailableScheduleMsg;

	private Logger log = LoggerFactory.getLogger(ScheduleBean.class);

	@Autowired
	private DashBean dashBean;

	@Autowired
	private IScheduleService scheduleService;

	public String getUploadPicutureScheduleMsg() {
		return uploadPicutureScheduleMsg;
	}

	public void setUploadPicutureScheduleMsg(String uploadPicutureScheduleMsg) {
		this.uploadPicutureScheduleMsg = uploadPicutureScheduleMsg;
	}

	public String getBookScheduleMsg() {
		return bookScheduleMsg;
	}

	public void setBookScheduleMsg(String bookScheduleMsg) {
		this.bookScheduleMsg = bookScheduleMsg;
	}

	public String getPictureAvailableScheduleMsg() {
		return pictureAvailableScheduleMsg;
	}

	public void setPictureAvailableScheduleMsg(
			String pictureAvailableScheduleMsg) {
		this.pictureAvailableScheduleMsg = pictureAvailableScheduleMsg;
	}

	public String getPictureAvailableStartDate() {
		return pictureAvailableStartDate;
	}

	public void setPictureAvailableStartDate(String pictureAvailableStartDate) {
		this.pictureAvailableStartDate = pictureAvailableStartDate;
	}

	public String getPictureAvailableEndDate() {
		return pictureAvailableEndDate;
	}

	public void setPictureAvailableEndDate(String pictureAvailableEndDate) {
		this.pictureAvailableEndDate = pictureAvailableEndDate;
	}

	public String getUploadImageStartDate() {
		return uploadImageStartDate;
	}

	public void setUploadImageStartDate(String uploadImageStartDate) {
		this.uploadImageStartDate = uploadImageStartDate;
	}

	public String getUploadImageEndDate() {
		return uploadImageEndDate;
	}

	public void setUploadImageEndDate(String uploadImageEndDate) {
		this.uploadImageEndDate = uploadImageEndDate;
	}

	public String getBookStartDate() {
		return bookStartDate;
	}

	public void setBookStartDate(String bookStartDate) {
		this.bookStartDate = bookStartDate;
	}

	public String getBookEndDate() {
		return bookEndDate;
	}

	public void setBookEndDate(String bookEndDate) {
		this.bookEndDate = bookEndDate;
	}

	public IScheduleService getScheduleService() {
		return scheduleService;
	}

	public void setScheduleService(IScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public DashBean getDashBean() {
		return dashBean;
	}

	public void setDashBean(DashBean dashBean) {
		this.dashBean = dashBean;
	}

	@PostConstruct
	public void init() {
		log.debug(" schedule bean init called!");
		schedule = scheduleService.getCurrentGroupSchedule(dashBean
				.getCurrentEventId());
		if (schedule == null) {
			log.debug("schedule not exists!");
			schedule = new Schedule();
			schedule.setEvent(dashBean.getCurrentEvent());
		}
	}

	public String saveOrUpdateSchedule() {

		FacesMessage msg = null;

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		log.debug("picture Available start date:" + pictureAvailableStartDate);
		log.debug("picture Available end date:" + pictureAvailableEndDate);

		log.debug("upload image start date:" + uploadImageStartDate);
		log.debug("upload image end date:" + uploadImageEndDate);

		log.debug("book start date:" + bookStartDate);
		log.debug("book end date:" + bookEndDate);

		try {

			schedule.setPictureAvailableStartDatetime(format
					.parse(pictureAvailableStartDate));

			schedule.setPictureAvailableEndDatetime(format
					.parse(pictureAvailableEndDate));

			schedule.setUploadImageStartDatetime(format
					.parse(uploadImageStartDate));

			schedule.setUploadImageEndDatetime(format.parse(uploadImageEndDate));

			schedule.setBookStartDatetime(format.parse(bookStartDate));

			schedule.setBookEndDatetime(format.parse(bookEndDate));

			schedule.setBookScheduleMessage(bookScheduleMsg);

			schedule.setPictureAvailableScheduleMessage(pictureAvailableScheduleMsg);

			schedule.setUploadImageScheduleMessage(uploadPicutureScheduleMsg);

			schedule.setCreatedBy(dashBean.getCurrentEventSecurityUser()
					.getSecurityUser().getSecurityUserId());

			schedule = scheduleService.saveOrUpdateSchedule(schedule);
			if (schedule != null && schedule.getScheduleId() > 0) {

				msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Schedule is Updated!", "");
			} else {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error while executing! please try again!", "");
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Execution Excpetion! please try again!", "");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);

		return null;
	}
}
