package com.coombu.photobook.dao;

import com.coombu.photobook.model.EventTemplate;

public interface IEventTemplateDao {

	public abstract void save(EventTemplate transientInstance);

	public abstract void delete(EventTemplate persistentInstance);

	public abstract EventTemplate merge(EventTemplate detachedInstance);

	public abstract EventTemplate findById(int id);

}