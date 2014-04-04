package com.coombu.photobook.dto;

import java.io.Serializable;
import java.util.Date;

public class ImageDetailsDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long imageId;
	
	private Long eventId;
	
	

	private String fileId;
	
	private String fileName;

	private String filePath;

	
	private Date uploadDateTime;
	
	
	private long fileSize;

	private int imageWidth;
	
	private int imageHeight;
	
	
	private int fileTypeId;

	
	private String eventDescription;

	private int imageSourceId;

	private int imageStatusTypeId;
	
	
	private String originalFileName;
	
	private int commentCount;
	
	private int voteCount;
	
	private int tagCount;
	
	private int viewCount;

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getUploadDateTime() {
		return uploadDateTime;
	}

	public void setUploadDateTime(Date uploadDateTime) {
		this.uploadDateTime = uploadDateTime;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public int getFileTypeId() {
		return fileTypeId;
	}

	public void setFileTypeId(int fileTypeId) {
		this.fileTypeId = fileTypeId;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public int getImageSourceId() {
		return imageSourceId;
	}

	public void setImageSourceId(int imageSourceId) {
		this.imageSourceId = imageSourceId;
	}

	public int getImageStatusTypeId() {
		return imageStatusTypeId;
	}

	public void setImageStatusTypeId(int imageStatusTypeId) {
		this.imageStatusTypeId = imageStatusTypeId;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	public int getTagCount() {
		return tagCount;
	}

	public void setTagCount(int tagCount) {
		this.tagCount = tagCount;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	

	

	
	

}
