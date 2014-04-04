package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.coombu.photobook.service.IImageService;
import com.coombu.photobook.service.IUserReportService;

@Named
@Scope(value = "view")
public class UserActivityReportBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fromDate;

	private String toDate;

	@Autowired
	IUserReportService userService;

	@Autowired
	DashBean dashBean;

	@Autowired
	IImageService imageService;

	private Logger log = LoggerFactory.getLogger(UserActivityReportBean.class);

	private List<Object[]> resultReport;

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	@PostConstruct
	public void init() {
		log.debug("init called! user activity report based on the group");
		resultReport = userService.getAllUsersActivityReports(
				dashBean.getCurrentEventId(), null, null);
	}

	public String doSearch() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date firstDate = null;
		Date secondDate = null;

		log.debug("From Date: " + fromDate);
		log.debug("To Date: " + toDate);

		if (fromDate.length() == 0 && toDate.length() == 0) {

			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Please select either one of the date to get the report!",
									""));

			return "";
		} else {

			if (fromDate != null || toDate != null) {

				try {

					if (fromDate != null && fromDate.length() != 0) {
						firstDate = (Date) format.parse(fromDate);
						log.debug("From Date: " + firstDate.toString());
					}

					if (toDate != null && toDate.length() != 0) {
						secondDate = (Date) format.parse(toDate);
						log.debug("To Date: " + secondDate.toString());
					}

					resultReport = userService
							.getAllUsersActivityReports(
									dashBean.getCurrentEventId(), firstDate,
									secondDate);

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Error! please make sure that your given input dates are correct?",
											""));
				}
			}

			if (resultReport.size() != 0) {
				log.debug("Records found for usage activity report!"
						+ resultReport.toString());
			} else {
				log.debug("Records not found for usage activity report!");
				resultReport = null;
			}

			return "";

		}

	}

	public String getSecurityUserName(Long eventSecUserId) {
		return (userService.getSecurityUserById(eventSecUserId)).getUserName();
	}

	public List<Object[]> getResultReport() {
		return resultReport;
	}

	public void setResultReport(List<Object[]> resultReport) {
		this.resultReport = resultReport;
	}

	public String getActiveCommentCount(long evenSecurityUserId) {
		return imageService.getCommentCount(evenSecurityUserId,
				dashBean.getCurrentEventId())
				+ "";
	}
}
