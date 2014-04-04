package com.coombu.photobook.dto;

import java.util.List;

public class PhotoBookDTO {
	
	String albumName;
	
	String albumDescription;
	
	List <Integer> images;
	
	Integer eventId;

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumDescription() {
		return albumDescription;
	}

	public void setAlbumDescription(String albumDescription) {
		this.albumDescription = albumDescription;
	}

	public List<Integer> getImages() {
		return images;
	}

	public void setImages(List<Integer> images) {
		this.images = images;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	
	

}
