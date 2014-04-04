package com.coombu.photobook.dao;

import com.coombu.photobook.model.EventCollege;

public interface IEventCollegeDao {

	public abstract void save(EventCollege transientInstance);

	public abstract void delete(EventCollege persistentInstance);

	public abstract EventCollege merge(EventCollege detachedInstance);

	public abstract EventCollege findById(int id);

}