package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.ImageSource;

public interface IImageSource {

	public abstract void save(ImageSource transientInstance);

	public abstract void delete(ImageSource persistentInstance);

	public abstract ImageSource merge(ImageSource detachedInstance);

	public abstract ImageSource findById(short id);

}