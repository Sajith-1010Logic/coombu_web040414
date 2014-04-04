package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.EventTypeTable;

public interface IEventTypeTable {

	public abstract void save(EventTypeTable transientInstance);

	public abstract void delete(EventTypeTable persistentInstance);

	public abstract EventTypeTable merge(EventTypeTable detachedInstance);

	public abstract EventTypeTable findById(short id);

}