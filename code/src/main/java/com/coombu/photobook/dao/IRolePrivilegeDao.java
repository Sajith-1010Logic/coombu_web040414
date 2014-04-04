package com.coombu.photobook.dao;

import com.coombu.photobook.model.RolePrivilege;

public interface IRolePrivilegeDao {

	public abstract void save(RolePrivilege transientInstance);

	public abstract void delete(RolePrivilege persistentInstance);

	public abstract RolePrivilege merge(RolePrivilege detachedInstance);

	public abstract RolePrivilege findById(int id);

}