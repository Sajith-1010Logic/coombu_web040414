package com.coombu.photobook.dao;

import com.coombu.photobook.model.Book;

public interface IBookDao {

	public abstract void save(Book transientInstance);

	public abstract void delete(Book persistentInstance);

	public abstract Book merge(Book detachedInstance);

	public abstract Book findById(int id);

}