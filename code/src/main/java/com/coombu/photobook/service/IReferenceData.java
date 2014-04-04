package com.coombu.photobook.service;

import java.util.List;

import com.coombu.photobook.model.lookup.Country;
import com.coombu.photobook.model.lookup.FileType;
import com.coombu.photobook.model.lookup.RequestReason;
import com.coombu.photobook.model.lookup.Role;
import com.coombu.photobook.model.lookup.UserStatus;

public interface IReferenceData 
{	
	/**
	 * initialize all lists on application start up
	 */
	public void init();

	public List<Country> getCountryList();
	
	public List<Role> getRoleList();
	
	public List<UserStatus>getUserStatusList();

	public String getRoleName(short roleId);

	public List<FileType> getFileTypeList();
	
	public List<RequestReason> getRequestReasonList();

	public String getReason(int requestReasonId);

}
