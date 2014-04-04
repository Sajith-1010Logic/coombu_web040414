package com.coombu.photobook.dao;

import com.coombu.photobook.model.Template;

public interface ITemplateDao {

	public abstract void save(Template transientInstance);

	public abstract void delete(Template persistentInstance);

	public abstract Template merge(Template detachedInstance);

	public abstract Template findById(short id);

}