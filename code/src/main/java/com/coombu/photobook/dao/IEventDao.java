package com.coombu.photobook.dao;

import com.coombu.photobook.model.Event;

public interface IEventDao {

	public abstract void save(Event transientInstance);

	public abstract void delete(Event persistentInstance);

	public abstract Event merge(Event detachedInstance);

	public abstract Event findById(long id);

}