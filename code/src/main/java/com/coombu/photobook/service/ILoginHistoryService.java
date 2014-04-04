package com.coombu.photobook.service;

import java.util.Date;
import java.util.List;

import com.coombu.photobook.model.LoginHistory;

public interface ILoginHistoryService {

	public List<LoginHistory> getAllHistoryFromOrNowAndToDate(long currentEventId, Date fromDate,
			Date toDate);

}
