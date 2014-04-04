package com.coombu.photobook.dao;

import com.coombu.photobook.model.UserAddress;

public interface IUserAddressDao {

	public abstract void save(UserAddress transientInstance);

	public abstract void delete(UserAddress persistentInstance);

	public abstract UserAddress merge(UserAddress detachedInstance);

	public abstract UserAddress findById(int id);

}