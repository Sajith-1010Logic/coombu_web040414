package com.coombu.photobook.dao;

import com.coombu.photobook.model.Phone;

public interface IPhoneDao {

	public abstract void save(Phone transientInstance);

	public abstract void delete(Phone persistentInstance);

	public abstract Phone merge(Phone detachedInstance);

	public abstract Phone findById(int id);

}