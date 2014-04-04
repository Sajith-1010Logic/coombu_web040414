package com.coombu.photobook.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.model.CollegeRegistrationRequest;
import com.coombu.photobook.model.lookup.Country;
import com.coombu.photobook.service.IReferenceData;
import com.coombu.photobook.service.IRegistrationService;

@Named("collegeManagementBean")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CollegeManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String firstname, lastname, collegename, address1, address2, city,
			state, primaryno, secondaryno, email;
	private int country;
	private byte[] imagebytes;
	private short collegeyear;
	private Date availabledate;
	private byte[] scannedidpicture;
	private List<Country> countryList;

	private Logger log = LoggerFactory.getLogger(UserManagementBean.class);

	@Autowired
	IRegistrationService collegeService;

	@Autowired
	private IReferenceData referenceData;

	@PostConstruct
	public void init() {
		countryList = referenceData.getCountryList();

		Iterator<Country> iterator = countryList.iterator();
		log.debug("C management init is called!");
	}

	@Transactional
	public void createNewCollege() {

		createCollege();
	}

	private void createCollege() {
		// TODO Auto-generated method stub

		CollegeRegistrationRequest college = new CollegeRegistrationRequest();

		college.setFirstName(firstname);
		college.setLastName(lastname);
		college.setCollegeName(collegename);
		college.setAddress1(address1);
		college.setAddress2(address2);
		college.setCity(city);
		college.setState(state);
		college.setCountry(country);
		college.setCollegeYear((short) 2014);
		college.setCreatedBy((long) 1);
		college.setCreateTimestamp(new Timestamp(new java.util.Date().getTime()));
		college.setUpdatedBy((long) 1);
		college.setUpdateTimestamp(new Timestamp(new java.util.Date().getTime()));
		college.setEmail(email);
		college.setPrimaryContactNumber(primaryno);
		college.setSecondaryContactNumber(secondaryno);
		college.setAvailableCallDateTime(new Timestamp(0));
		college.setScannedIdPicture(imagebytes);
		collegeService.requestRegistration(college);

		FacesMessage msg = new FacesMessage(
				"College Request Send successfully!...");

		FacesContext.getCurrentInstance().addMessage(null, msg);

		reset();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("addDialog.hide();");
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCollegename() {
		return collegename;
	}

	public void setCollegename(String collegename) {
		this.collegename = collegename;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPrimaryno() {
		return primaryno;
	}

	public void setPrimaryno(String primaryno) {
		this.primaryno = primaryno;
	}

	public String getSecondaryno() {
		return secondaryno;
	}

	public void setSecondaryno(String secondaryno) {
		this.secondaryno = secondaryno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public short getCollegeyear() {
		return collegeyear;
	}

	public void setCollegeyear(short collegeyear) {
		this.collegeyear = collegeyear;
	}

	public Date getAvailabledate() {
		return availabledate;
	}

	public void setAvailabledate(Date availabledate) {
		this.availabledate = availabledate;
	}

	public byte[] getScannedidpicture() {
		return scannedidpicture;
	}

	public void setScannedidpicture(byte[] scannedidpicture) {
		this.scannedidpicture = scannedidpicture;
	}

	public List<Country> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}

	public void upload(FileUploadEvent event) {

		FacesMessage msg = new FacesMessage("Success! ", event.getFile()
				.getFileName() + " is uploaded.");

		FacesContext.getCurrentInstance().addMessage(null, msg);

		try {

			copyFile(event.getFile().getFileName(), event.getFile()
					.getInputstream());

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public void copyFile(String fileName, InputStream in) {

		try {

			imagebytes = IOUtils.toByteArray(in);

		} catch (IOException e) {

			System.out.println(e.getMessage());

		}

	}

	public void reset() {

		address1 = null;
		address2 = null;
		availabledate = null;
		city = null;
		collegename = null;
		collegeyear = 0;
		country = 0;
		email = null;
		firstname = null;
		imagebytes = null;
		lastname = null;
		state = null;
		primaryno = null;
		secondaryno = null;

	}

}
