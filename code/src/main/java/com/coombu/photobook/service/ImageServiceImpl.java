package com.coombu.photobook.service;

import static com.coombu.photobook.util.Constants.DEFAULT_BUFFER_SIZE;
import static com.coombu.photobook.util.Constants.MAX_FILE_SIZE;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.imgscalr.AsyncScalr;
import org.imgscalr.Scalr;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.dao.ICommentDao;
import com.coombu.photobook.dao.IImageDao;
import com.coombu.photobook.dao.IImageTagDao;
import com.coombu.photobook.dao.IImageVoteDao;
import com.coombu.photobook.dao.IRemovalRequestDao;
import com.coombu.photobook.dao.exception.DuplicateImageDaoException;
import com.coombu.photobook.dao.exception.DuplicateTagDaoException;
import com.coombu.photobook.dao.exception.DuplicateVoteDaoException;
import com.coombu.photobook.dto.FriendDTO;
import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageTag;
import com.coombu.photobook.model.ImageVote;
import com.coombu.photobook.model.RemovalRequest;
import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.model.lookup.FileType;
import com.coombu.photobook.service.exception.CoombuServiceException;
import com.coombu.photobook.service.exception.DuplicateImageException;
import com.coombu.photobook.service.exception.DuplicateTagException;
import com.coombu.photobook.service.exception.DuplicateVoteException;
import com.coombu.photobook.service.exception.ImageServiceException;
import com.coombu.photobook.util.Constants.COMMENT_STATUS_TYPE;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;
import com.coombu.photobook.util.Constants.SUBMENU_TYPE;
import com.coombu.photobook.webservice.exception.EventAccessNotAllowed;
import com.coombu.photobook.webservice.model.CommentRequest;
import com.coombu.photobook.webservice.model.ImageTagRequest;
import com.coombu.photobook.webservice.model.LikeRequest;

@Service("imageService")
@Transactional
public class ImageServiceImpl implements IImageService, Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

	@Autowired
	IReferenceData referenceData;

	@Autowired
	IImageDao imageDao;

	@Autowired
	ICommentDao commentDao;

	@Autowired
	IImageVoteDao voteDao;

	@Autowired
	IImageTagDao tagDao;

	@Autowired
	private IRemovalRequestDao removalRequestDao;

	private @Value("${image.root.dir}")
	String imageRoot;

	private @Value("${image.root.crop.dir}")
	String cropImageRoot;

	@Override
	public List<Image> getEventImages(SUBMENU_TYPE type, long eventId,
			Integer pageNumber, Integer pageSize) {

		return imageDao.getImages(type, eventId, pageNumber, pageSize);

	}

	private String getFileName(String path, String fileName, String type) {
		StringBuffer fullPath = new StringBuffer(this.imageRoot);
		fullPath.append(path);
		fullPath.append(File.separator);
		if (type != null) {
			fullPath.append(type);
		}
		fullPath.append(fileName);
		return fullPath.toString();
	}

	@Async
	void resize(Image img) {
		try {
			String fullSizeFile = getFileName(img.getFilePath(),
					img.getFileName(), null);
			String mobileFile = getFileName(img.getFilePath(),
					img.getFileName(), "mobile-");
			String lightboxFile = getFileName(img.getFilePath(),
					img.getFileName(), "lightbox-");
			String dashboardFile = getFileName(img.getFilePath(),
					img.getFileName(), "dashboard-");
			String activityFile = getFileName(img.getFilePath(),
					img.getFileName(), "activity-");

			log.debug("loading original file {}", System.currentTimeMillis());

			BufferedImage bimg = ImageIO.read(new File(fullSizeFile));
			log.debug(
					"done loading orginal file - starting thumbnail scaling {}",
					System.currentTimeMillis());

			img.setImageHeight(bimg.getHeight());
			img.setImageWidth(bimg.getWidth());
			log.debug(", Image Width: {} , Image Height: {}",
					img.getImageWidth(), img.getImageHeight());

			// lightbox - height 600px
			BufferedImage scaledImage = AsyncScalr.resize(bimg,
					Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 600).get();
			log.debug("done lightbox scaling - starting writing thumbnail {}",
					System.currentTimeMillis());
			ImageIO.write(scaledImage, "jpg", new File(lightboxFile));

			// mobile
			log.debug("done writing thumbnail - starting scaling mobile {}",
					System.currentTimeMillis());
			scaledImage = AsyncScalr.resize(bimg, 450).get();
			log.debug("done mobile scaling - starting writing mobile {}",
					System.currentTimeMillis());
			ImageIO.write(scaledImage, "jpg", new File(mobileFile));

			log.debug("done writing mobile - starting scaling dashboard {}",
					System.currentTimeMillis());

			// dashboard -- set width to 300
			scaledImage = AsyncScalr.resize(bimg, Scalr.Method.QUALITY,
					Scalr.Mode.FIT_TO_WIDTH, 300).get();
			log.debug("done dashboard scaling - starting writing dashboard {}",
					System.currentTimeMillis());
			ImageIO.write(scaledImage, "jpg", new File(dashboardFile));
			log.debug("done writing dashboard starting scaling activity{}",
					System.currentTimeMillis());

			// activiy - set width to 200
			scaledImage = AsyncScalr.resize(bimg, Scalr.Method.QUALITY,
					Scalr.Mode.FIT_TO_WIDTH, 200).get();
			log.debug("done dashboard scaling - starting writing dashboard {}",
					System.currentTimeMillis());
			ImageIO.write(scaledImage, "jpg", new File(activityFile));

		} catch (IOException | IllegalArgumentException | ImagingOpException
				| InterruptedException | ExecutionException ioe) {
			log.error("Error: ", ioe);
		}

	}

	@Override
	public void uploadSavePicture(Image img) throws ImageServiceException {
		uploadPicture(img);
		imageDao.save(img);
	}

	@Override
	public void uploadPicture(Image image) throws ImageServiceException {
		String hash = null;
		UploadedFile file;
		try {
			file = image.getUploadedFile();
			image.setFileTypeId(getFileTypeId(file));
			String extension = "";
			int i = file.getFileName().lastIndexOf('.');
			if (i <= 0 || image.getFileTypeId() == null) {
				log.error(
						"file does not have an extension or approved content type. It is not an image file: {}",
						file.getFileName());
				throw new ImageServiceException("File is not an image");
			} else {
				extension = file.getFileName().substring(i);

			}
			image.setFileSize((int) file.getSize());
			if (image.getFileSize() > MAX_FILE_SIZE) {
				log.error(
						"file size larger than MAX_FILE_SIZE: {} actual size {}",
						MAX_FILE_SIZE, image.getFileSize());
				throw new ImageServiceException(
						"File size is greater than allowed size");

			}
			hash = DigestUtils.md5Hex(file.getInputstream());
			StringBuffer fileStr = new StringBuffer(hash.substring(0, 2));
			fileStr.append(File.separatorChar).append(hash.substring(2, 4));
			fileStr.append(File.separatorChar).append(hash.substring(4, 6));
			fileStr.append(File.separatorChar);
			image.setFilePath(fileStr.toString());

			fileStr.append(hash).append(extension);
			image.setFileName(hash + extension);
			image.setFileId(hash);
			File savedFile = saveFile(file, imageRoot + fileStr.toString());
			if (savedFile.length() <= 0)
				throw new ImageServiceException(
						"File length is 0 - file not uploaded");
			image.setFileSize(savedFile.length());
			log.debug("Uploaded file size: {}", savedFile.length());
			/*
			 * java.awt.Image im =
			 * Toolkit.getDefaultToolkit().getImage(imageRoot +
			 * fileStr.toString()); image.setImageWidth((short)
			 * im.getWidth(null)); image.setImageHeight((short)
			 * im.getHeight(null));
			 * log.debug("File Size: {}, Image Width: {} , Image Height: {}",
			 * savedFile.length(), im.getWidth(null), im.getHeight(null));
			 */
			resize(image);
		} catch (IOException ioe) {
			log.error("Error saving uploaded file {}", hash, ioe);
		} catch (Exception e) {
			log.error("Error saving uploaded file {}", hash, e);
			throw new ImageServiceException(
					"Error writing file to file system:" + e.getMessage(), e);
		}

	}

	private Short getFileTypeId(UploadedFile file) {
		Short id = null;
		for (FileType fileType : referenceData.getFileTypeList()) {
			if (fileType.getFileTypeName().equals(file.getContentType())) {
				id = fileType.getFileTypeId();
			}
		}
		return id;
	}

	private File saveFile(UploadedFile uploadedFile, String fileName)
			throws Exception {
		InputStream is = null;
		File file = null;
		BufferedOutputStream output = null;
		try {
			file = new File(fileName);
			if (file.exists()) {
				return file;
			}
			file.getParentFile().mkdirs();
			file.createNewFile();

			FileOutputStream fos = new FileOutputStream(file);
			is = uploadedFile.getInputstream();

			output = new BufferedOutputStream(fos, DEFAULT_BUFFER_SIZE);
			byte[] b = new byte[DEFAULT_BUFFER_SIZE];
			int read = 0;
			while ((read = is.read(b)) != -1) {
				output.write(b, 0, read);
			}
		} catch (Exception e) {
			log.error("Error writing file {} to file system", fileName);
			throw (e);
		} finally {
			if (is != null)
				is.close();
			if (output != null)
				output.close();
		}
		return file;
	}

	@Override
	public List<Image> getPendingImages(int securityUserId, int eventId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void approveImages(List<Image> images) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tagUsers(List<EventSecurityUser> eventSecurityUserList,
			long imageId, long eventId, long securityUserId) {
		this.tagDao.saveTagList(eventSecurityUserList, imageId, eventId,
				securityUserId);
	}

	@Override
	public List<FriendDTO> getUsersInGroup(SecurityUser securityUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyTaggedPicture(Image image, List<SecurityUser> users) {
		// TODO Auto-generated method stub

	}

	@Override
	public void voteForPicture(Image image, SecurityUser user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void comment(Image image) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestImageRemoval(RemovalRequest removalRequest) {
		removalRequestDao.save(removalRequest);
		Image img = this.getImage(removalRequest.getImageCommentId(), false);
		img.setImageStatusTypeId(IMAGE_STATUS_TYPE.PENDING.id());
		imageDao.update(img);
		if (commentDao.inActivateCommentsForThisImage(img.getImageId())) {
			log.debug("comments of this image are in inactive mode!");
		}

	}
	@Override
	public void requestCommentRemoval(RemovalRequest req) 
	{
		removalRequestDao.save(req);
		Comment cmt = commentDao.findById((int) req.getImageCommentId());
		if (cmt != null) 
		{
			cmt.setCommentStatusId(COMMENT_STATUS_TYPE.FLAGED.id());
			commentDao.update(cmt);
		}
	}

	@Override
	public void RemoveComment(int commentId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(String fileName, long eventId) {
		return imageDao.getImage(fileName, eventId);

	}

	@Override
	public void save(List<Image> fileList) throws DuplicateImageException {
		try {
			imageDao.save(fileList);
		} catch (DuplicateImageDaoException dide) {
			log.error("Duplicate image : ", dide);
			throw new DuplicateImageException(dide.getMessage());
		}

	}

	@Override
	public int getEventImagesCount(SUBMENU_TYPE type, Long eventId) {
		return imageDao.getImagesCount(type, eventId);
	}

	@Override
	public List<ImageVote> getImageVote(long imageId) {
		return imageDao.getImageVote(imageId);

	}

	@Override
	public List<ImageVote> getImageVoteByUser(int eventSecurityUserId) {
		return imageDao.getImageVoteByUser(eventSecurityUserId);
	}

	@Override
	public List<ImageVote> getImageVoteByEvent(Long eventId) {
		return imageDao.getImageVoteByEvent(eventId);
	}

	@Override
	public Image getImage(long imageId, boolean isLoadComments) {
		return imageDao.getImage(imageId, isLoadComments);
	}

	@Override
	public List<Image> getEventImages(SUBMENU_TYPE subMenuType, long eventId) {
		return imageDao.getImages(subMenuType, eventId, null, null);
	}

	/**
	 * Retrieves all images uploaded by a user for an event
	 */
	@Override
	public List<Image> getEventImages(long eventSecurityUserId, long eventId) {
		return imageDao.getImages(eventSecurityUserId, eventId);
	}

	public List<Image> getEventImages(SUBMENU_TYPE type,
			long eventSecurityUserId, long eventId) {
		return imageDao.getImages(type, eventSecurityUserId, eventId);
	}

	@Override
	public int deleteImage(long imageId, long eventSecurityUserId) {
		int code = -1;
		Image image = imageDao.findById(imageId);
		if (image.getEventSecurityUser().getEventSecurityUserId() == eventSecurityUserId) {
			imageDao.delete(image);
			if (commentDao.deleteCommentsForThisImage(image.getImageId())) {
				log.debug("comments are deleted!");
			}
			code = 0;
		} else {
			code = 2;
		}
		return code;
	}

	@Override
	public void addComment(CommentRequest comment) {
		commentDao.saveWS(comment);
	}

	@Override
	public void addTag(ImageTagRequest tag) throws DuplicateTagException {
		try {
			if(tag.getStatus().equalsIgnoreCase("tag"))
			this.tagDao.saveWS(tag);
			else if (tag.getStatus().equalsIgnoreCase("untag"))
			this.tagDao.removeThisTag(tag.getImageId(), tag.getEventId(), tag.getEventSecurityUserId());
		} catch (DuplicateTagDaoException dte) {
			log.error("Tag is duplicate: ", dte);
			throw new DuplicateTagException(dte.getMessage());
		}
	}

	@Override
	public void addLike(LikeRequest like) throws DuplicateVoteException {
		try {
			if(like.getStatus().equalsIgnoreCase("like"))
			this.voteDao.saveWS(like);
			else if(like.getStatus().equalsIgnoreCase("unlike"))
			this.voteDao.unLike(like);
			/*
			 * if (imageDao.incrementImageVoteCount(like.getImageId())) {
			 * log.debug("image like count is incremented!"); }
			 */

		} catch (DuplicateVoteDaoException dve) {
			log.error("Vote is duplicate: ", dve);
			throw new DuplicateVoteException(dve.getMessage());
		}
	}

	@Override
	public List<RemovalRequest> getFlaggedComments(long eventId) {
		return removalRequestDao.getFlaggedComments(eventId);
	}

	@Override
	public List<RemovalRequest> getFlaggedImages(long eventId) {
		return removalRequestDao.getFlaggedImages(eventId);
	}

	@Override
	public void repost(long imageId, long eventId, short approveId)
			throws CoombuServiceException {
		Image img = getImage(imageId, false);
		if (img == null) {
			throw new CoombuServiceException("The image is not available");
		}
		if (img.getEvent().getEventId() != eventId) {
			throw new EventAccessNotAllowed(
					"The user does not have access to the image: "
							+ img.getImageId(), "");
		}
		img.setImageStatusTypeId(IMAGE_STATUS_TYPE.APPROVED.id());
		imageDao.save(img);
		removalRequestDao.delete(removalRequestDao.findByCommentId((int)imageId));

	}

	@Override
	public int getImageCount(long eventSecurityId, long eventId) {
		return imageDao.getImagesCount(eventSecurityId, eventId);
	}

	@Override
	public int getCommentCount(long eventSecurityId, long eventId) {
		return commentDao.getCommentCount(eventSecurityId, eventId);
	}

	@Override
	public List<ImageTag> getImageTags(long imageId, long eventId) {
		return tagDao.getImageTags(imageId, eventId);
	}

	@Override
	public void repostComment(long commentId, long removalRequestId, short approveId)
			throws CoombuServiceException {
		Comment cmt = getComment(commentId);
		if (cmt == null) {
			throw new CoombuServiceException("The comment is not available");
		}

		cmt.setCommentStatusId(approveId);
		commentDao.save(cmt);
		removalRequestDao.repostComment(removalRequestId);

	}

	@Override
	public Comment getComment(long commentId) {
		return commentDao.getAllComments(commentId);
	}

	@Override
	public List<Comment> getEventComments(long securityUserId, long eventId) {
		return commentDao.getComments(securityUserId, eventId);
	}

	@Override
	public int flagImage(long imageId, long eventSecurityUserId,RemovalRequest removalRequest) {
		RemovalRequest rq =removalRequestDao.findByCommentId((int)imageId);
		if(rq==null){
			removalRequestDao.save(removalRequest);
		}
		return imageDao.flagImage(imageId, eventSecurityUserId);
	}

	@Override
	public int flagComment(long commentId, long eventSecurityUserId , RemovalRequest removalRequest) {
		RemovalRequest rr =removalRequestDao.findByCommentId((int)commentId);
		if(rr==null)
		 removalRequestDao.save(removalRequest);
		return commentDao.flagComment(commentId, eventSecurityUserId);
	}

	@Override
	public Image getImages(long imageId) {

		return imageDao.getImages(imageId);
	}
	
	@Override
	public boolean unLike(LikeRequest request) {

		if (voteDao.unLike(request)) {
			log.debug("unlike functionality is worked!");
			log.debug("decrementing vote count!");
			/*
			 * if (imageDao.decrementImageVoteCount(request.getImageId())) {
			 * log.debug("decrement is executed!"); }
			 */
			return true;
		}
		return false;
	}

	@Override
	public boolean checkImageLikeExistence(long imageId, long eventId,
			long eventSecUserId) {
		ImageVote vote = voteDao.checkImageLikeExistence(imageId, eventId,
				eventSecUserId);
		if (vote != null) {
			log.debug("You have alreay liked this image!");
			return true;
		}
		return false;
	}

	@Override
	public Comment addNewComment(Comment comment) {
		// TODO Auto-generated method stub
		return commentDao.merge(comment);
	}

	@Override
	public boolean checkCommentExistence(long imageId, long eventSecurityId) {

		return commentDao.checkCommentExistence(imageId, eventSecurityId);
	}

	@Override
	public boolean checkTagExistence(long imageId, long securityId) {
		// TODO Auto-generated method stub
		return tagDao.checkTagExistence(imageId, securityId);
	}

	/**
	 * New functionality to upload picture by accepting byte array by sajith V.J
	 */
	@Override
	public void uploadImageWS(Image img, byte b[]) throws ImageServiceException {
		uploadPictureWS(img, b);
		imageDao.save(img);

	}

	/**
	 * Functionality for uploading image getting as base64 string
	 * 
	 * @param image
	 * @param b
	 * @throws ImageServiceException
	 */

	public void uploadPictureWS(Image image, byte b[])
			throws ImageServiceException {
		String hash = null;

		try {

			image.setFileTypeId((short) 1);
			String extension = "";
			int i = image.getFileName().lastIndexOf('.');
			if (i <= 0 || image.getFileTypeId() == null) {
				log.error(
						"file does not have an extension or approved content type. It is not an image file: {}",
						image.getFileName());
				throw new ImageServiceException("File is not an image");
			} else {
				extension = image.getFileName().substring(i);

			}

			if (image.getFileSize() > MAX_FILE_SIZE) {
				log.error(
						"file size larger than MAX_FILE_SIZE: {} actual size {}",
						MAX_FILE_SIZE, image.getFileSize());
				throw new ImageServiceException(
						"File size is greater than allowed size");

			}
			hash = DigestUtils.md5Hex(b);
			StringBuffer fileStr = new StringBuffer(hash.substring(0, 2));
			fileStr.append(File.separatorChar).append(hash.substring(2, 4));
			fileStr.append(File.separatorChar).append(hash.substring(4, 6));
			fileStr.append(File.separatorChar);
			image.setFilePath(fileStr.toString());

			fileStr.append(hash).append(extension);
			image.setFileName(hash + extension);
			image.setFileId(hash);
			File savedFile = saveFile(b, imageRoot + fileStr.toString());
			if (savedFile.length() <= 0)
				throw new ImageServiceException(
						"File length is 0 - file not uploaded");
			image.setFileSize(savedFile.length());
			resize(image);

		} catch (IOException ioe) {
			log.error("Error saving uploaded file {}", hash, ioe);
		} catch (Exception e) {
			log.error("Error saving uploaded file {}", hash, e);
			throw new ImageServiceException(
					"Error writing file to file system:" + e.getMessage(), e);
		}

	}

	private File saveFile(byte[] is, String fileName) throws Exception {

		File file = null;

		try {
			file = new File(fileName);
			if (file.exists()) {
				return file;
			}
			file.getParentFile().mkdirs();
			file.createNewFile();

			FileOutputStream fis = new FileOutputStream(file);
			fis.write(is);

			fis.close();
		} catch (Exception e) {
			log.error("Error writing file {} to file system", fileName);
			throw (e);
		}

		return file;
	}

	@Override
	public boolean deleteComment(int commentId) {
		Comment comment = commentDao.findById(commentId);
		if (comment != null) {
			commentDao.delete(comment);
			log.debug("comment is deleted!");
			return true;
		}
		log.debug("error in deletion!");
		return false;
	}

	@Override
	public boolean reportComment(int commentId, long eventSecurityUserId) {
		int res = commentDao.flagComment(commentId, eventSecurityUserId);
		if (res == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateDescription(Image image) {
		try {
			imageDao.update(image);
			log.debug("image is updated!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Set<Comment> getComments(long imageId) {
		return commentDao.getComments(imageId);
	}

	@Override
	public int getCommentCountByImage(long imageId) {

		return commentDao.getCommentCountByImageId(imageId);
	}

	@Override
	public boolean checkTagExistenceForUserToThisImage(long eventSecurityId,
			long imageId) {
		return tagDao.checkThisUserAlreadyBeenTaggedWithThisImage(imageId,
				eventSecurityId);
	}

	@Override
	public String getHashcode(UserProfile profile, CroppedImage cropImage) {

		String imageName = imageDao.getHashCodeOfTheImage(profile);

		try {
			storeCroppedImage(cropImage, cropImageRoot + "/" + imageName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageName;

	}

	private void storeCroppedImage(CroppedImage croppedImage, String fileName)
			throws IOException {

		File file = new File(fileName);
		file.getParentFile().mkdirs();
		if (file.exists()) {
			file.delete();
			file.createNewFile();
		}
		FileImageOutputStream imageOutput;

		try {
			imageOutput = new FileImageOutputStream(file);
			imageOutput.write(croppedImage.getBytes(), 0,
					croppedImage.getBytes().length);
			imageOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getImageName(UserProfile profile) {

		String imageName = imageDao.getHashCodeOfTheImage(profile);

		return imageName;

	}

	@Override
	public int getLikeCountBasedOnUser(long eventSecurityId, long eventId) {
		
		return imageDao.getImageLikeCountBasedOnUser(eventSecurityId, eventId);
	}

	
	@Override
	public String getOriginallFileName(String fileName) {
		
		return imageDao.getOriginalFileName(fileName);
	}

}
