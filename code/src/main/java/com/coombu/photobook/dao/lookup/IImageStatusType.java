package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.ImageStatusType;

public interface IImageStatusType {

	public abstract void save(ImageStatusType transientInstance);

	public abstract void delete(ImageStatusType persistentInstance);

	public abstract ImageStatusType merge(ImageStatusType detachedInstance);

	public abstract ImageStatusType findById(short id);

}