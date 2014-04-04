package com.coombu.photobook.dto;

import java.io.Serializable;

import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.UserProfile;

public class FlaggedImageDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Image Image;

	private UserProfile userProfile;
	
	private UserProfile flagedBy;

	private String reason;

	private String description;

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	/**
	 * @return the flagedBy
	 */
	public UserProfile getFlagedBy() {
		return flagedBy;
	}

	/**
	 * @param flagedBy the flagedBy to set
	 */
	public void setFlagedBy(UserProfile flagedBy) {
		this.flagedBy = flagedBy;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
