package com.coombu.photobook.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@Entity
@Table(name = "image_bucket_image")

public class ImageBucketImage implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "BUCKET_IMAGE_ID", unique = true, nullable = false)
	private Long imageBucketImageId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IMAGE_ID", nullable = false)
	private Image imageName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BUCKET_ID", nullable = false)
	private ImageBucket albumName;
	
	

	public Long getAlbumImageID() {
		return imageBucketImageId;
	}

	public void setAlbumImageID(Long albumImageID) {
		this.imageBucketImageId = albumImageID;
	}

	public Long getImageBucketImageId() {
		return imageBucketImageId;
	}

	public void setImageBucketImageId(Long imageBucketImageId) {
		this.imageBucketImageId = imageBucketImageId;
	}

	public Image getImageName() {
		return imageName;
	}

	public void setImageName(Image imageName) {
		this.imageName = imageName;
	}

	public ImageBucket getAlbumName() {
		return albumName;
	}

	public void setAlbumName(ImageBucket albumName) {
		this.albumName = albumName;
	}

	


}
