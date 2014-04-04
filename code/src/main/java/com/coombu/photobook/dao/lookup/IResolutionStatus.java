package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.ResolutionStatus;

public interface IResolutionStatus {

	public abstract void save(ResolutionStatus transientInstance);

	public abstract void delete(ResolutionStatus persistentInstance);

	public abstract ResolutionStatus merge(ResolutionStatus detachedInstance);

	public abstract ResolutionStatus findById(short id);

}