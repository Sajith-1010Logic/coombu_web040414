package com.coombu.photobook.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.dao.ICommentDao;
import com.coombu.photobook.dao.IImageDao;
import com.coombu.photobook.dao.IImageVoteDao;

@Service
@Transactional
public class UsageReportServiceImpl implements IUsageReportService {

	@Autowired
	IImageDao uploadImages;

	@Autowired
	ICommentDao comments;

	@Autowired
	IImageVoteDao imageVotes;

	private Logger log = LoggerFactory.getLogger(UsageReportServiceImpl.class);

	@Override
	public Long[] getUsageReportCount(Long eventId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		Long[] result = new Long[3];

		log.debug("getting usage reports for this group");

		result[0] = uploadImages.getUploadsCountByEvent(eventId, startDate,
				endDate);

		result[1] = comments.getAllCommentsCount(eventId, startDate, endDate);

		result[2] = imageVotes
				.getAllImageVoteCount(eventId, startDate, endDate);

		return result;
	}

}
