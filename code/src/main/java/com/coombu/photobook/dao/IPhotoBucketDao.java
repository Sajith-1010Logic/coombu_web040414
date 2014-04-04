package com.coombu.photobook.dao;

import java.util.List;

import com.coombu.photobook.model.ImageBucketImage;
import com.coombu.photobook.model.ImageBucket;

public interface IPhotoBucketDao {

	public abstract int save(ImageBucket transientInstance);

	public abstract void delete(ImageBucket persistentInstance);

	public abstract ImageBucket merge(ImageBucket detachedInstance);

	public abstract ImageBucket findById(int id);

	public abstract List<ImageBucket> findByEventId(int eventId);

	public abstract int saveAlbumImage(ImageBucketImage albumImage);

	public abstract List<ImageBucketImage> getAlbumImages(int eventID, int type);

	public abstract List<ImageBucketImage> getPartialySavedImages(
			int currentEventId);

	public abstract void removeImageIdsFromBucket(int ImageId);

}