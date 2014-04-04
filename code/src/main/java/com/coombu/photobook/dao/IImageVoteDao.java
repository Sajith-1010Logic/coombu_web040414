package com.coombu.photobook.dao;

import java.util.Date;
import com.coombu.photobook.dao.exception.DuplicateVoteDaoException;
import com.coombu.photobook.model.ImageVote;
import com.coombu.photobook.webservice.model.LikeRequest;

public interface IImageVoteDao {

	public abstract void save(ImageVote transientInstance);

	public abstract void saveWS(LikeRequest transientInstance)
			throws DuplicateVoteDaoException;

	public abstract void delete(ImageVote persistentInstance);

	public abstract ImageVote merge(ImageVote detachedInstance);

	public abstract ImageVote findById(int id);

	public abstract Long getAllImageVoteCount(Long eventId, Date startDate,
			Date endDate);

	public abstract boolean unLike(LikeRequest likeRequest);

	public abstract ImageVote checkImageLikeExistence(long imageId,
			long eventId, long eventSecUserId);
}