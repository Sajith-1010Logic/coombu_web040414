package com.coombu.photobook.dao;

import com.coombu.photobook.model.Address;

public interface IAddressDao {

	public abstract void save(Address transientInstance);

	public abstract void delete(Address persistentInstance);

	public abstract Address merge(Address detachedInstance);

	public abstract Address findById(int id);

}