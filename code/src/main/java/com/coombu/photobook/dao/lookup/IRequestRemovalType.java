package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.RequestRemovalType;

public interface IRequestRemovalType {

	public abstract void save(RequestRemovalType transientInstance);

	public abstract void delete(RequestRemovalType persistentInstance);

	public abstract RequestRemovalType merge(RequestRemovalType detachedInstance);

	public abstract RequestRemovalType findById(int id);

}