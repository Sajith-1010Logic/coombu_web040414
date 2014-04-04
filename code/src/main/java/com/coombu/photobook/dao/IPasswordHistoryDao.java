package com.coombu.photobook.dao;

import com.coombu.photobook.model.PasswordHistory;

public interface IPasswordHistoryDao {

	public abstract void save(PasswordHistory transientInstance);

	public abstract void delete(PasswordHistory persistentInstance);

	public abstract PasswordHistory merge(PasswordHistory detachedInstance);

	public abstract PasswordHistory findById(int id);

}