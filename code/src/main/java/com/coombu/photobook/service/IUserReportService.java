package com.coombu.photobook.service;

import java.util.Date;
import java.util.List;

import com.coombu.photobook.model.SecurityUser;

public interface IUserReportService {
	
	public List<Object[]> getAllUsersActivityReports(long eventId, Date startDate, Date endDate);
	
	public SecurityUser getSecurityUserById(long eventSecurityUserId);
}
