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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.coombu.photobook.model.LoginHistory;
import com.coombu.photobook.service.ILoginHistoryService;
import com.coombu.photobook.service.LoginHistoryServiceImpl;
import com.coombu.photobook.util.DateHelper;

@Named
@Scope("view")
public class LoginAndLogoutReportBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fromDate;

	private String toDate;

	@Autowired
	private DashBean dashBean;

	private List<LoginHistory> historyList;

	public List<LoginHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<LoginHistory> historyList) {
		this.historyList = historyList;
	}

	@Autowired
	ILoginHistoryService loginHistoryService;

	private org.slf4j.Logger log = org.slf4j.LoggerFactory
			.getLogger(LoginAndLogoutReportBean.class);

	public void setDashBean(DashBean dashBean) {
		this.dashBean = dashBean;
	}

	public DashBean getDashBean() {
		return dashBean;
	}

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
		historyList = null;
		historyList = loginHistoryService.getAllHistoryFromOrNowAndToDate(dashBean.getCurrentEventId(),null,
				null);
	}

	// Action method to performing search.
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

					historyList = loginHistoryService
							.getAllHistoryFromOrNowAndToDate(dashBean.getCurrentEventId(),
									firstDate,
									secondDate != null ? DateHelper
											.getDateEndTime(secondDate)
											: secondDate);

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

				if (historyList != null && !historyList.isEmpty()) {
					log.debug("History list is not empty! it has "
							+ historyList.size());
				} else {
					log.debug("History list empty!");
					historyList = null;
				}
			}

			return "";
		}
	}

	public ILoginHistoryService getLoginHistoryService() {
		return loginHistoryService;
	}

	public void setLoginHistoryService(
			LoginHistoryServiceImpl loginHistoryService) {
		log.debug("calling the set login history service");
		this.loginHistoryService = loginHistoryService;
	}
}
