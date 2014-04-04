package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.AddressType;

public interface IAddressType {

	public abstract void save(AddressType transientInstance);

	public abstract void delete(AddressType persistentInstance);

	public abstract AddressType merge(AddressType detachedInstance);

	public abstract AddressType findById(short id);

}