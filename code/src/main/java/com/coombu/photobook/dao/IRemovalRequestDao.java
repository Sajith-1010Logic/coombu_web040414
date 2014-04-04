package com.coombu.photobook.dao;

import java.util.List;

import com.coombu.photobook.model.RemovalRequest;

public interface IRemovalRequestDao {

	public abstract void save(RemovalRequest transientInstance);

	public abstract void delete(RemovalRequest persistentInstance);

	public abstract RemovalRequest merge(RemovalRequest detachedInstance);

	public abstract RemovalRequest findById(int id);

	List<RemovalRequest> getFlaggedComments(long eventId);

	List<RemovalRequest> getFlaggedImages(long eventId);
	
	public abstract RemovalRequest findByCommentId(int id);

	public abstract void repostComment(long commentId);

}