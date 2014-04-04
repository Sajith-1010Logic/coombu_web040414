package com.coombu.photobook.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.dao.ILoginHistoryDao;
import com.coombu.photobook.model.LoginHistory;

@Service("loginAndLogoutService")
@Transactional
public class LoginHistoryServiceImpl implements ILoginHistoryService {

	@Autowired
	ILoginHistoryDao loginHistoryDao;

	@Override
	public List<LoginHistory> getAllHistoryFromOrNowAndToDate(long currentEventId,Date fromDate,
			Date toDate) {
		// TODO Auto-generated method stub
		return loginHistoryDao
				.getAllHistoryFromOrNowAndToDate(currentEventId,fromDate, toDate);
	}

}
