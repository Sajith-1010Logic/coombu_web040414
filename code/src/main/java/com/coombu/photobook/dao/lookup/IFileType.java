package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.FileType;

public interface IFileType {

	public abstract void save(FileType transientInstance);

	public abstract void delete(FileType persistentInstance);

	public abstract FileType merge(FileType detachedInstance);

	public abstract FileType findById(short id);

}