package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.RequestReason;

public interface IRequestReason {

	public abstract void save(RequestReason transientInstance);

	public abstract void delete(RequestReason persistentInstance);

	public abstract RequestReason merge(RequestReason detachedInstance);

	public abstract RequestReason findById(int id);

}