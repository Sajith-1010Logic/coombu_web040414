package com.coombu.photobook.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "image_bucket")
public class ImageBucket {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "IMAGE_BUCKET_ID", unique = true, nullable = false)
	private Long imageBucketID;
	
	@Column(name = "IMAGE_BUCKET_NAME")
	private String imageBucketName;
	
	@Column(name = "IMAGE_BUCKET_DESCRIPTION")
	private String imageBucketDescription;
	
	@Column(name = "BOOK_ID")
	private int bookID;
	
	@Column(name = "IMAGE_BUCKET_VISIBILITY")
	private int imageBucketVisibility;
	
	@Column(name = "BUCKET_FILE_PATH")
	private String bucketfilePath;

	@Column(name="EVENT_ID")
	private int eventID;
	
	@Column(name="IMAGE_STATUS_TYPE")
	private int imageStatusType;
	
	
	
	public Long getImageBucketID() {
		return imageBucketID;
	}

	public void setImageBucketID(Long imageBucketID) {
		this.imageBucketID = imageBucketID;
	}

	public String getImageBucketDescription() {
		return imageBucketDescription;
	}

	public void setImageBucketDescription(String imageBucketDescription) {
		this.imageBucketDescription = imageBucketDescription;
	}

	public int getBookID() {
		return bookID;
	}

	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	public int getImageBucketVisibility() {
		return imageBucketVisibility;
	}

	public void setImageBucketVisibility(int imageBucketVisibility) {
		this.imageBucketVisibility = imageBucketVisibility;
	}

	public String getBucketfilePath() {
		return bucketfilePath;
	}

	public void setBucketfilePath(String bucketfilePath) {
		this.bucketfilePath = bucketfilePath;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public String getImageBucketName() {
		return imageBucketName;
	}

	public void setImageBucketName(String imageBucketName) {
		this.imageBucketName = imageBucketName;
	}

	public int getImageStatusType() {
		return imageStatusType;
	}

	public void setImageStatusType(int imageStatusType) {
		this.imageStatusType = imageStatusType;
	}

	
	
	
}
