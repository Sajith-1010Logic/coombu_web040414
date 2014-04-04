package com.coombu.photobook.dao;

import com.coombu.photobook.model.SecurityUserRole;

public interface ISecurityUserRoleDao {

	public abstract void save(SecurityUserRole transientInstance);

	public abstract void delete(SecurityUserRole persistentInstance);

	public abstract SecurityUserRole merge(SecurityUserRole detachedInstance);

	public abstract SecurityUserRole findById(int id);

}