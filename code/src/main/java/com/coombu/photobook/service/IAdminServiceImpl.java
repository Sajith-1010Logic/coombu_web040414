package com.coombu.photobook.service;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.dao.ICommentDao;
import com.coombu.photobook.dao.IImageDao;
import com.coombu.photobook.dao.IRemovalRequestDao;
import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.RemovalRequest;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;

@Service("imageAdminService")
@Transactional
public class IAdminServiceImpl implements IAdminService, Serializable {
	@Autowired
	IImageDao imageDao;

	@Autowired
	ICommentDao commentDao;

	@Autowired
	IRemovalRequestDao removalRequestDao;

	private Logger log = LoggerFactory.getLogger(IAdminServiceImpl.class);

	private Image image;

	private RemovalRequest rrquest;

	@Override
	public List<RemovalRequest> getImageRemovalRequest(long eventId) {
		return removalRequestDao.getFlaggedImages(eventId);
	}

	@Override
	public void approveImageRemoval(List<RemovalRequest> removalRequst) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<RemovalRequest> getCommentRemovalRequest(long eventId) {
		return removalRequestDao.getFlaggedComments(eventId);
	}

	@Override
	public void approveCommentRemoval(List<RemovalRequest> removalRequst) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Image> getApprovalImages(IMAGE_STATUS_TYPE type,
			Integer pageNumber, long eventId, Integer pageSize) {

		return imageDao.getAllImages(type, eventId, pageNumber, pageSize);
	}

	@Override
	public int getImagesCount(long l) {
		return imageDao.getAdminImagesCount(l);
	}

	@Override
	public void deleteImage(long imageId) {
		setImage(imageDao.findById(imageId));
		String Id = String.valueOf(imageId);
		setRrquest(removalRequestDao.findByCommentId(Integer.valueOf(Id)));
		imageDao.delete(getImage());
		removalRequestDao.delete(getRrquest());
		if(commentDao.deleteCommentsForThisImage(imageId)){
			log.debug("comment deletion is successfull while you reposting the image!");
		}
	}

	@Override
	public void repostImage(long imageId) {
		setImage(imageDao.findById(imageId));
		String Id = String.valueOf(imageId);
		setRrquest(removalRequestDao.findByCommentId(Integer.valueOf(Id)));
		image.setImageStatusTypeId(IMAGE_STATUS_TYPE.APPROVED.id());
		imageDao.update(getImage());
		removalRequestDao.delete(getRrquest());
		if (commentDao.activateAllTheCommentsForThisImage(imageId)) {
			log.debug("comment activation is successfull while you reposting the image!");
		}

	}

	@Override
	public List<Comment> getFlaggedComments(long eventId, long eventSecurityId) {
		return commentDao.getFlagComments(eventId, eventSecurityId);
	}

	public IImageDao getImageDao() {
		return imageDao;
	}

	public void setImageDao(IImageDao imageDao) {
		this.imageDao = imageDao;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public IRemovalRequestDao getRemovalRequestDao() {
		return removalRequestDao;
	}

	public void setRemovalRequestDao(IRemovalRequestDao removalRequestDao) {
		this.removalRequestDao = removalRequestDao;
	}

	public RemovalRequest getRrquest() {
		return rrquest;
	}

	public void setRrquest(RemovalRequest rrquest) {
		this.rrquest = rrquest;
	}

	public ICommentDao getCommentDao() {
		return commentDao;
	}

	public void setCommentDao(ICommentDao commentDao) {
		this.commentDao = commentDao;
	}

}
