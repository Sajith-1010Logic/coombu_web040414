package com.coombu.photobook.dto;

import java.io.Serializable;

import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.UserProfile;

public class FlaggedCommentDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long RemovalRequestId;
	
	private Comment comment;
	
	private UserProfile userProfile;
	
	private UserProfile flagedBy;
	
	private String reason;
	
	private String description;

	private String fileName;

	public Comment getComment() {
		return comment;
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

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public void setFileName(String fileName) 
	{
		this.fileName = fileName;
	}
	public String getFileName()
	{
		return fileName;
	}

	public long getRemovalRequestId() {
		return RemovalRequestId;
	}

	public void setRemovalRequestId(long removalRequestId) {
		RemovalRequestId = removalRequestId;
	}
	
	

}
