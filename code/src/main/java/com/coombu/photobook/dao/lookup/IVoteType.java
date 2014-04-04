package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.VoteType;

public interface IVoteType {

	public abstract void save(VoteType transientInstance);

	public abstract void delete(VoteType persistentInstance);

	public abstract VoteType merge(VoteType detachedInstance);

	public abstract VoteType findById(int id);

}