package com.coombu.photobook.dto;

import java.io.Serializable;

import javax.persistence.Column;

public class EventMembersDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private long imageCount;
	
private long commentCount;
	
private long voteCount;
	
private long tagCount;

private String firstName;

private String lastName;

private String imageData;

public long getImageCount() {
	return imageCount;
}

public void setImageCount(long imageCount) {
	this.imageCount = imageCount;
}

public long getCommentCount() {
	return commentCount;
}

public void setCommentCount(long commentCount) {
	this.commentCount = commentCount;
}

public long getVoteCount() {
	return voteCount;
}

public void setVoteCount(long voteCount) {
	this.voteCount = voteCount;
}

public long getTagCount() {
	return tagCount;
}

public void setTagCount(long tagCount) {
	this.tagCount = tagCount;
}

public String getFirstName() {
	return firstName;
}

public void setFirstName(String firstName) {
	this.firstName = firstName;
}

public String getLastName() {
	return lastName;
}

public void setLastName(String lastName) {
	this.lastName = lastName;
}

public String getImageData() {
	return imageData;
}

public void setImageData(String imageData) {
	this.imageData = imageData;
}



}
