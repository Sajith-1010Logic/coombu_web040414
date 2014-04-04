package com.coombu.photobook.service;

import java.util.List;

import com.coombu.photobook.dto.PhotoBookDTO;
import com.coombu.photobook.model.ImageBucketImage;
import com.coombu.photobook.model.ImageBucket;

public interface IPhotoBookSevice {

	public abstract int SavePhotoBook(PhotoBookDTO photobucket, int type);

	public abstract List<ImageBucket> getPhotoBook(int eventID);

	public abstract List<ImageBucketImage> getAlbumImages(int eventID, int type);

	public abstract List<ImageBucketImage> getPartialySavedImages(
			int currentEventId);

	public abstract int modifyPhotoBook(ImageBucket imageBucket,
			List<Integer> selectedListIndex, int type);

	public abstract boolean removeUnCheckedImagesFromBucket(List<Integer> ids);

}
