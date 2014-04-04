package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.PhoneType;

public interface IPhoneType {

	public abstract void save(PhoneType transientInstance);

	public abstract void delete(PhoneType persistentInstance);

	public abstract PhoneType merge(PhoneType detachedInstance);

	public abstract PhoneType findById(short id);

}