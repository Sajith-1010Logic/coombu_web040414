package com.coombu.photobook.service;

import static com.coombu.photobook.util.Constants.DEFAULT_BUFFER_SIZE;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.dao.IImageDao;
import com.coombu.photobook.dao.IPhotoBucketDao;
import com.coombu.photobook.dto.PhotoBookDTO;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageBucket;
import com.coombu.photobook.model.ImageBucketImage;

@Service("photoBookService")
@Transactional
public class PhotoBookServiceImpl implements IPhotoBookSevice {

	private static Logger log = LoggerFactory
			.getLogger(PhotoBookServiceImpl.class);

	@Autowired
	IImageDao imageDao;

	@Autowired
	IPhotoBucketDao photoBicketDao;

	private @Value("${image.root.dir}")
	String imageRoot;

	private @Value("${image.photobucket.dir}")
	String photoBucketDir;

	private ImageBucket imgB;

	@Override
	public int SavePhotoBook(PhotoBookDTO photobucket, int type) {
		try {

			ImageBucket imgBucket = new ImageBucket();
			imgBucket.setImageBucketDescription(photobucket
					.getAlbumDescription());
			imgBucket.setBucketfilePath(photoBucketDir
					+ photobucket.getAlbumName() + File.separator);
			imgBucket.setImageBucketVisibility(1);
			imgBucket.setEventID(photobucket.getEventId());
			imgBucket.setBookID(1);
			imgBucket.setImageStatusType(type);
			imgBucket.setImageBucketName(photobucket.getAlbumName());
			imgB = photoBicketDao.merge(imgBucket);
			getPictures(photobucket.getImages(), photobucket.getAlbumName(),
					photobucket.getEventId());
			if (imgB != null)
				return 1;
			else
				return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public int modifyPhotoBook(ImageBucket imageBucket,
			List<Integer> selectedListIndex, int type) {
		try {
			imageBucket.setImageStatusType(type);

			imgB = photoBicketDao.merge(imageBucket);
			getPictures(selectedListIndex, imageBucket.getImageBucketName(),
					imageBucket.getEventID());

			if (imgB != null) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public void getPictures(List<Integer> images, String name, int eventId)
			throws Exception {

		if (images != null) {

			for (Integer imageID : images) {
				Image image = new Image();
				image = imageDao.findById((long) imageID);
				String sourceFullPath = imageRoot + image.getFilePath()
						+ image.getFileName();
				String destinationFullPath = photoBucketDir + name
						+ File.separator + image.getFileName();
				saveFile(sourceFullPath, destinationFullPath, name,
						image.getFileName(), eventId, image);
			}
		}

	}

	private void saveFile(String source, String destination, String albumname,
			String imagename, int eventId, Image image) throws Exception {
		File file1 = new File(source);
		InputStream is = new FileInputStream(file1);
		File file = null;
		BufferedOutputStream output = null;

		try {
			file = new File(destination);
			if (file.exists()) {
				log.error("already exists", file);
				ImageBucket imgBucket = photoBicketDao.findByEventId(eventId)
						.get(0);
				ImageBucketImage albumImage = new ImageBucketImage();
				albumImage.setAlbumName(imgB);
				albumImage.setImageName(image);
				int i = photoBicketDao.saveAlbumImage(albumImage);

				// return file;
			} else {
				file.getParentFile().mkdirs();
				file.createNewFile();

				FileOutputStream fis = new FileOutputStream(file);
				output = new BufferedOutputStream(fis, DEFAULT_BUFFER_SIZE);
				byte[] b = new byte[DEFAULT_BUFFER_SIZE];
				int read = 0;

				while ((read = is.read(b)) != -1) {
					output.write(b, 0, read);

				}
				ImageBucket imgBucket = photoBicketDao.findByEventId(eventId)
						.get(0);
				ImageBucketImage albumImage = new ImageBucketImage();
				albumImage.setAlbumName(imgB);
				albumImage.setImageName(image);
				int i = photoBicketDao.saveAlbumImage(albumImage);
			}
		} catch (Exception e) {
			log.error("Error writing file {} to file system", "alert.png");
			throw (e);
		} finally {
			if (is != null)
				is.close();
			if (output != null)
				output.close();
		}
		// return file;
	}

	public IImageDao getImageDao() {
		return imageDao;
	}

	public void setImageDao(IImageDao imageDao) {
		this.imageDao = imageDao;
	}

	public IPhotoBucketDao getPhotoBicketDao() {
		return photoBicketDao;
	}

	public void setPhotoBicketDao(IPhotoBucketDao photoBicketDao) {
		this.photoBicketDao = photoBicketDao;
	}

	@Override
	public List<ImageBucket> getPhotoBook(int eventID) {
		return photoBicketDao.findByEventId(eventID);
	}

	@Override
	public List<ImageBucketImage> getAlbumImages(int eventID, int type) {

		return photoBicketDao.getAlbumImages(eventID, type);
	}

	public ImageBucket getImgB() {
		return imgB;
	}

	public void setImgB(ImageBucket imgB) {
		this.imgB = imgB;
	}

	@Override
	public List<ImageBucketImage> getPartialySavedImages(int currentEventId) {

		return photoBicketDao.getPartialySavedImages(currentEventId);
	}

	@Override
	public boolean removeUnCheckedImagesFromBucket(List<Integer> ids) {

		if (ids != null) {
			for (Integer imageId : ids) {
				photoBicketDao.removeImageIdsFromBucket(imageId);
			}
		}
		if (ids != null) {
			ids.clear();
		}
		return true;
	}

}
