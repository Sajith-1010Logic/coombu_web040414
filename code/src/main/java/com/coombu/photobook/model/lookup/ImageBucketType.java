package com.coombu.photobook.model.lookup;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "image_bucket_lkp", uniqueConstraints = {
		@UniqueConstraint(columnNames = "IMAGE_BUCKET_TYPE_NAME"),
		@UniqueConstraint(columnNames = "IMAGE_BUCKET_TYPE_DESCRIPTION") })
public class ImageBucketType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "IMAGE_BUCKET_TYPE_ID", unique = true, nullable = false)
	private short imageBucketId;

	@Column(name = "IMAGE_BUCKET_TYPE_NAME", unique = true, nullable = false, length = 15)
	private String imageBucketName;
	
	@Column(name = "IMAGE_BUCKET_TYPE_DESCRIPTION", unique = true, nullable = false, length = 45)
	private String imageBucketDescription;

	public short getImageBucketId() {
		return imageBucketId;
	}

	public void setImageBucketId(short imageBucketId) {
		this.imageBucketId = imageBucketId;
	}

	public String getImageBucketName() {
		return imageBucketName;
	}

	public void setImageBucketName(String imageBucketName) {
		this.imageBucketName = imageBucketName;
	}

	public String getImageBucketDescription() {
		return imageBucketDescription;
	}

	public void setImageBucketDescription(String imageBucketDescription) {
		this.imageBucketDescription = imageBucketDescription;
	}
	
	

}
