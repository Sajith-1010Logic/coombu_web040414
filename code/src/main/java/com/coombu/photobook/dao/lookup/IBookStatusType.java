package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.BookStatusType;

public interface IBookStatusType {

	public abstract void save(BookStatusType transientInstance);

	public abstract void delete(BookStatusType persistentInstance);

	public abstract BookStatusType merge(BookStatusType detachedInstance);

	public abstract BookStatusType findById(byte id);

}