package com.coombu.photobook.dao;

import com.coombu.photobook.model.CommentVote;

public interface ICommentVoteDao {

	public abstract void save(CommentVote transientInstance);

	public abstract void delete(CommentVote persistentInstance);

	public abstract CommentVote merge(CommentVote detachedInstance);

	public abstract CommentVote findById(int id);

}