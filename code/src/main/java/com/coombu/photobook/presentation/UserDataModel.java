package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.presentation.search.UserSearchCriteria;
import com.coombu.photobook.service.IUserService;

public class UserDataModel extends LazyDataModel<SecurityUser> implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UserSearchCriteria searchCriteria;

	IUserService userService;

	List<SecurityUser> searchResultList;

	private SecurityUser selected;

	private List<SecurityUser> activatedUsers = new ArrayList<SecurityUser>();

	public UserDataModel(UserSearchCriteria searchCriteria1,
			IUserService userService1) {
		this.searchCriteria = searchCriteria1;
		this.userService = userService1;
	}

	@Override
	public List<SecurityUser> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, String> filters) {
		searchCriteria.setCurrentPage(first / pageSize + 1);
		searchCriteria.setNumRowsReturned(pageSize);
		setFilter(filters);
		searchResultList = userService.findUser(searchCriteria, first,
				sortField, sortOrder.equals(SortOrder.ASCENDING));
		return searchResultList;
	}

	private void setFilter(Map<String, String> filters) {
		if (filters != null && filters.containsKey("globalFilter")) {
			String filter = filters.get("globalFilter");
			if ( filter.startsWith("User Type ="))
			{
				searchCriteria.setUserType(filter.replace("User Type = ", ""));
			}
			else
			{
				searchCriteria.setLastName(filters.get("globalFilter"));
				searchCriteria.setFirstName(filters.get("globalFilter"));
				searchCriteria.setEmailAddress(filters.get("globalFilter"));
			}
		}

	}

	@Override
	public int getRowCount() {
		if (super.getRowCount() == 0) {
			int count = userService.getNumberOfUsers(searchCriteria);
			super.setRowCount(count);
		}
		return super.getRowCount();
	}

	public SecurityUser getSelected() {
		return selected;
	}

	public void setSelected(SecurityUser selected1) {
		this.selected = selected1;
	}

	public int getCurrentPage() {
		return this.searchCriteria.getCurrentPage();
	}

	/*public int getPageSize() {
		return this.searchCriteria.getPageSize();
	}*/

	public List<SecurityUser> getSearchResultList() {
		return searchResultList;
	}

	/**
	 * @param activatedUsers the activatedUsers to set
	 */
	public void setActivatedUsers(List<SecurityUser> activatedUsers) {
		this.activatedUsers = activatedUsers;
	}

	/**
	 * @return the activatedUsers
	 */
	public List<SecurityUser> getActivatedUsers() {
		return activatedUsers;
	}
}