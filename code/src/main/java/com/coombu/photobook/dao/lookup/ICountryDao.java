package com.coombu.photobook.dao.lookup;

import java.util.List;

import com.coombu.photobook.model.lookup.Country;

public interface ICountryDao {

	public abstract void save(Country transientInstance);

	public abstract void delete(Country persistentInstance);

	public abstract Country merge(Country detachedInstance);

	public abstract Country findById(short id);

	public abstract List<Country> getAll();

}