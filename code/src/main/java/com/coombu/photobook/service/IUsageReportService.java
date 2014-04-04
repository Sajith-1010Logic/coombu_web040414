package com.coombu.photobook.service;

import java.util.Date;

public interface IUsageReportService {
	
	public Long[] getUsageReportCount(Long eventId, Date startDate, Date endDate);
	
}
