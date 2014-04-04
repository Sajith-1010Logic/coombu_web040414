package com.coombu.photobook.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.coombu.photobook.model.Comment;
import com.coombu.photobook.webservice.model.CommentRequest;

public interface ICommentDao extends IDao{

	public abstract void save(Comment transientInstance);

	public abstract void delete(Comment persistentInstance);

	public abstract Comment merge(Comment detachedInstance);

	public abstract Comment findById(int id);

	void saveWS(CommentRequest transientInstance);

	public abstract int getCommentCount(long eventSecurityId, long eventId);

	public abstract Comment getAllComments(long commentId);

	public abstract List<Comment> getComments(long eventSecurityId, long eventId);

	public abstract int flagComment(long commentId, long eventSecurityUserId);

	public abstract Long getAllCommentsCount(Long eventId, Date startDate,
			Date endDate);

	public abstract boolean checkCommentExistence(long imageId,
			long eventSecurityId);

	public abstract List<Comment> getFlagComments(long eventId,
			long eventSecurityId);

	public abstract Set<Comment> getComments(long imageId);

	public abstract int getCommentCountByImageId(long imageId);

	public abstract boolean deleteCommentsForThisImage(long imageId);

	public abstract boolean inActivateCommentsForThisImage(long imageId);

	public abstract boolean activateAllTheCommentsForThisImage(long imageId);
}