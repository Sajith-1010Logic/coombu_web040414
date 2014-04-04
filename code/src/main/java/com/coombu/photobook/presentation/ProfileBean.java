package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.coombu.photobook.model.Address;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.Phone;
import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.model.UserAddress;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.service.IImageService;
import com.coombu.photobook.service.IUserService;
import com.coombu.photobook.util.CoombuUtil;

@Named("profileBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProfileBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(ProfileBean.class);

	@Autowired
	IUserService userService;

	@Autowired
	IImageService imageService;

	UserProfile profile;

	SecurityUser securityUser;

	CroppedImage image;

	Address address = new Address();

	Phone phone = new Phone(profile, (short) 1);

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	public SecurityUser getSecurityUser() {
		return securityUser;
	}

	public void setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}

	/**
	 * @return the phone
	 */
	public Phone getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public void loadData() {
		long securityUserId = CoombuUtil.getSecurityUserId();
		profile = userService.getUserProfile(securityUserId);
		if (!profile.getUserAddresses().isEmpty()) {
			address = ((UserAddress) profile.getUserAddresses().toArray()[0])
					.getAddress();
		}
		if (!profile.getPhones().isEmpty()) {
			phone = ((Phone) profile.getPhones().toArray()[0]);
		}

	}

	public void loadSecurityUser() {
		String userName = CoombuUtil.getSecurityUserName();
		securityUser = userService.findUser(userName);
	}

	public void changePassword() {
		userService.updatePassword(securityUser, securityUser.getUserName());
	}

	public void save() {

		setAddress();
		setPhone();
		userService.saveUserProfile(profile);
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile Updated",
						"Profile Updated"));
	}

	private void setPhone() {
		if (profile.getPhones().isEmpty()) {
			phone.setUserProfile(profile);
			profile.getPhones().add(phone);
		}

	}

	private void setAddress() {
		if (profile.getUserAddresses().isEmpty()) {
			UserAddress uadd = new UserAddress();
			uadd.setAddressTypeId((short) 1);
			uadd.setUserProfile(profile);
			uadd.setAddress(address);
			Set<UserAddress> uaddSet = new HashSet<UserAddress>();
			uaddSet.add(uadd);
			profile.setUserAddresses(uaddSet);
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		Image img = new Image();
		img.setUploadedFile(event.getFile());
		try {
			imageService.uploadPicture(img);
			profile.setImageFileName(img.getFileName());
			userService.saveUserProfile(profile);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Error uploading profile picture:", e);

		}

	}

	public void handleCropFileUpload() {

		// System.out.println("GET Bytes" + croppedImage.getBytes());

		String imageName = imageService.getHashcode(profile, image);
		profile.setCroppedImageName(imageName);
		userService.saveUserProfile(profile);

	}

	public CroppedImage getImage() {
		return image;
	}

	public void setImage(CroppedImage image) {
		this.image = image;
	}

}
