package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.coombu.photobook.service.IUsageReportService;

@Named
@Scope(value = "view")
public class UsageActivityReportBean implements Serializable {

	/**
	 * 
	 */

	@Autowired
	IUsageReportService usageReportService;

	@Autowired
	DashBean dashBean;

	private static final long serialVersionUID = 1L;
	private org.slf4j.Logger log = org.slf4j.LoggerFactory
			.getLogger(UsageActivityReportBean.class);
	private String fromDate;

	private String toDate;

	private Long eventId;

	private Long[] counts;

	public void setCounts(Long[] counts) {
		this.counts = counts;
	}

	public Long[] getCounts() {
		return counts;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	@PostConstruct
	public void init() {
		eventId = dashBean.getCurrentEventId();
		log.debug("current event is: " + eventId);
		counts = usageReportService.getUsageReportCount(eventId, null, null);
		log.debug("Usgae report init is called!");
	}

	public String doSearch() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date firstDate = null;
		Date secondDate = null;

		log.debug("From Date: " + fromDate);
		log.debug("To Date: " + toDate);
		log.debug("event Id: " + eventId);

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

					counts = usageReportService.getUsageReportCount(eventId,
							firstDate, secondDate);

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

			if (counts.length != 0) {
				log.debug("Records found for usage activity report!");
			} else {
				log.debug("Records not found for usage activity report!");
			}

			return "";

		}

	}
}
