package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.CommentStatusType;

public interface ICommentStatusType {

	public abstract void save(CommentStatusType transientInstance);

	public abstract void delete(CommentStatusType persistentInstance);

	public abstract CommentStatusType merge(CommentStatusType detachedInstance);

	public abstract CommentStatusType findById(short id);

}