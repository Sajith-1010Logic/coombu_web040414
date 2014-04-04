package com.coombu.photobook.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.dao.IEventSecurityUserDao;
import com.coombu.photobook.dao.IImageDao;
import com.coombu.photobook.model.SecurityUser;

@Service
@Transactional
public class UserReportServiceImpl implements IUserReportService {

	@Autowired
	IImageDao images;

	@Autowired
	IEventSecurityUserDao securityUser;

	@Override
	public List<Object[]> getAllUsersActivityReports(long eventId,
			Date startDate, Date endDate) {
		return images.getAllUsersActivityReport(eventId, startDate, endDate);
	}

	@Override
	public SecurityUser getSecurityUserById(long eventSecurityUserId) {
		// TODO Auto-generated method stub
		return securityUser.getEventSecurityUserById(eventSecurityUserId);
	}

}
