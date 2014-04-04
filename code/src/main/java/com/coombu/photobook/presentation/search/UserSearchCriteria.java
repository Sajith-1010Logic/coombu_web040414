package com.coombu.photobook.presentation.search;

import java.io.Serializable;

import javax.faces.model.DataModel;

import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.presentation.UserDataModel;
import com.coombu.photobook.service.IUserService;

/**
 * A backing bean for the Facility list page. Encapsulates the criteria needed
 * to perform a Facility list
 */
public class UserSearchCriteria implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The maximum page size of the Facility Search result list
	 */
	//private int pageSize = 200;

	/**
	 * Number of rows to return
	 */
	private int numRowsReturned = 10;
	/**
	 * The page the user is currently on.
	 */
	private int currentPage = 1;

	/**
	 * The criterion to search for
	 */
	private String userType = "";
	private String lastName = "";
	private String firstName = "";
	private String zip = "";
	private String sortBy = "";
	private String emailAddress = "";

	/**
	 * Returns a {@link DataModel} based on the search criteria.
	 * 
	 * @param facilitySearchService
	 *            the service to use to retrieve facilities.
	 */
	public DataModel<SecurityUser> getDataModel(IUserService userService) {
		return new UserDataModel(this, userService);
	}

/*	
	public int getPageSize() { return pageSize; }
	  
	public void setPageSize(int pageSize1) { this.pageSize = pageSize1; }
*/	 
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage1) {
		this.currentPage = currentPage1;
	}
/*
	public void validateEnterSearchCriteria(ValidationContext context) {
		
		 * MessageContext messages = context.getMessageContext(); if
		 * (this.communitySize == 0 && this.speciality == 0 && this.region == 0)
		 * { messages.addMessage(new
		 * MessageBuilder().error().source(null).code("public.search.criteria"
		 * ).build()); }
		 
	}
*/
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName1
	 *            the lastName to set
	 */
	public void setLastName(String lastName1) {
		this.lastName = lastName1;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName1
	 *            the firstName to set
	 */
	public void setFirstName(String firstName1) {
		this.firstName = firstName1;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip1
	 *            the zip to set
	 */
	public void setZip(String zip1) {
		this.zip = zip1;
	}

	/**
	 * @return the sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * @param sortBy1
	 *            the sortBy to set
	 */
	public void setSortBy(String sortBy1) {
		this.sortBy = sortBy1;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType
	 *            the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getNumRowsReturned() {
		return numRowsReturned;
	}

	public void setNumRowsReturned(int numRowsReturned1) {
		this.numRowsReturned = numRowsReturned1;
	}

	public String toString() {

		StringBuilder str = new StringBuilder(
				"\n[User Search Criteria searchString = '");
		str.append("User Type     = ").append(this.userType).append("\n");
		str.append("Last Name     = ").append(this.lastName).append("\n");
		str.append("First Name    = ").append(this.firstName).append("\n");
		str.append("Zip           = ").append(this.zip).append("\n");
		str.append("sort By       = ").append(this.sortBy).append("\n");
		str.append("Email Address = ").append(this.emailAddress)
				.append("\n]\n");

		return str.toString();
	}

}

