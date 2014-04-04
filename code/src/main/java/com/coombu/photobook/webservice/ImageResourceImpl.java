package com.coombu.photobook.webservice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.coombu.photobook.dto.EventMembersDTO;
import com.coombu.photobook.dto.GetEventsDTO;
import com.coombu.photobook.dto.ImageDetailsDTO;
import com.coombu.photobook.dto.ImageTagDTO;
import com.coombu.photobook.model.Address;
import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageTag;
import com.coombu.photobook.model.Phone;
import com.coombu.photobook.model.RemovalRequest;
import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.model.UserAddress;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.service.IImageService;
import com.coombu.photobook.service.IUserService;
import com.coombu.photobook.util.Constants;
import com.coombu.photobook.util.Constants.COMMENT_STATUS_TYPE;
import com.coombu.photobook.util.Constants.IMAGE_SOURCE;
import com.coombu.photobook.util.Constants.IMAGE_STATUS_TYPE;
import com.coombu.photobook.util.Constants.REQUEST_REMOVAL_TYPE;
import com.coombu.photobook.util.Constants.RESOLUTION_STATUS_TYPE;
import com.coombu.photobook.util.Constants.SUBMENU_TYPE;
import com.coombu.photobook.util.CoombuUtil;
import com.coombu.photobook.webservice.exception.CoombuWebServiceException;
import com.coombu.photobook.webservice.exception.EventAccessNotAllowed;
import com.coombu.photobook.webservice.model.CommentRequest;
import com.coombu.photobook.webservice.model.DownloadImage;
import com.coombu.photobook.webservice.model.ImageTagRequest;
import com.coombu.photobook.webservice.model.LikeRequest;
import com.coombu.photobook.webservice.model.RepostCommentRequest;
import com.coombu.photobook.webservice.model.RepostRequest;
import com.coombu.photobook.webservice.model.UploadImage;
import com.google.gson.Gson;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.multipart.MultiPart;
import com.thoughtworks.xstream.core.util.Base64Encoder;

@Component
@Path("/photobook")
public class ImageResourceImpl implements IImageResource {
	private static final String SUCCESS = "Success";

	// private static final int NOT_AUTHORIZED_CODE = 2;

	private static final String NOT_AUTHORIZED = "Not Authorized";

	private static final int SUCCESS_CODE = 0;
	
	private @Value("${image.root.dir}") String imageRoot;
	
	private static Logger log = LoggerFactory.getLogger(ImageResourceImpl.class);

	@Autowired
	IImageService imageService;

	@Autowired
	IUserService userService;

	@Context
	ServletContext context;

	private int flagReasonId;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getLogin")
	public Map<String, Object> getLogin(@QueryParam("userId") String userId,
			@QueryParam("password") String password) {

		log.debug("Entered getLogin - Parameters userId: {}, password: {}",
				userId, password);
		
		Map<String, Object> data = new HashMap<String, Object>();

		try {
			SecurityUser code = userService.wsLogin(userId, password);
			if(code!=null){
			data.put("responseCode",SUCCESS_CODE);
			data.put("responseString", SUCCESS);
		    data.put( "securityUserId", code.getSecurityUserId());
		    data.put( "userName",code.getUserName());
		 
		  
//		  
			}
			else{
				data.put("responseCode", 1);
				data.put("responseString", NOT_AUTHORIZED);
				data.put( "securityUserId", null);
			    data.put( "userName",null);
				
			}
		} catch (Exception e) {
			data.put("responseCode", 1);
			data.put("responseString", NOT_AUTHORIZED);
			data.put( "securityUserId", null);
		    data.put( "userName",null);
			
		}
		Map<String, Object> treeMap = new TreeMap<String,Object>(data);
		
		return treeMap;

	}


	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getAllImages")
	public List<ImageDetailsDTO> getImages(@QueryParam("subMenuType") int subMenuTypeId,
			@QueryParam("eventId") long eventId)
			throws CoombuWebServiceException {
		log.debug(
				"Entered getImages : parameters subMenuTypeId: {}, eventId: {}",
				subMenuTypeId, eventId);
		
		try {
			if (!userService.isEventIdAllowedAccess(eventId)) {
//				throw new EventAccessNotAllowed(
//						"The user does not have access to the event: "
//								+ eventId, "");
				
				throw new CoombuWebServiceException("The user does not have access to the event: "
						+ eventId);
			} else {
				SUBMENU_TYPE type = SUBMENU_TYPE.getSubMenuType(subMenuTypeId);
				List<Image> imageList = imageService.getEventImages(type,
						eventId);
				List<ImageDetailsDTO> imageDetailsList = getImageDetails(imageList);
				

				return imageDetailsList;
			}
		} catch (Exception e) {
			log.error("Error getting Images: ", e);
			throw new CoombuWebServiceException(e.getMessage());
		}

	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getEvents")
	public List<GetEventsDTO> getEvents() throws CoombuWebServiceException {
		try {
			log.debug("Entered getEvents : ");
			long securityUserId = CoombuUtil.getSecurityUserId();
			
			List<EventSecurityUser> eventList = userService
					.getEventSecurityUser(securityUserId);
			if(eventList!=null){
			List<GetEventsDTO> eventsDTOs = getEventList(eventList);
			return eventsDTOs;
			}
			else{
				throw new CoombuWebServiceException("User does not have any events");
			}
		} catch (Exception e) {
			throw new CoombuWebServiceException(e.getMessage());

		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getMyImages")
	public List<ImageDetailsDTO> getMyImages(@QueryParam("eventId") long eventId) throws CoombuWebServiceException {
		log.debug("Entered getImages : parameters eventId: {}", eventId);
		if (!userService.isEventIdAllowedAccess(eventId))
//			throw new EventAccessNotAllowed(
//					"The user does not have access to the event: " + eventId,
//					"");
			throw new CoombuWebServiceException("The user does not have access to the event: "
					+ eventId);
		long securityUserId = CoombuUtil.getSecurityUserId();
		EventSecurityUser eventSecurityUser = userService.getEventSecurityUser(
				securityUserId, eventId);
		List<Image> imageList = imageService.getEventImages(
				eventSecurityUser.getEventSecurityUserId(), eventId);
		List<ImageDetailsDTO> imageDetailsList = getImageDetails(imageList);
		return imageDetailsList;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getMembers")
	public List<EventMembersDTO> getMembers(
			@QueryParam("eventId") long eventId) throws CoombuWebServiceException {
		log.debug("Entered getImages : parameters eventId: {}", eventId);
		if (!userService.isEventIdAllowedAccess(eventId))
//			throw new EventAccessNotAllowed(
//					"The user does not have access to the event: " + eventId,
//					"");
			throw new CoombuWebServiceException("The user does not have access to the event: "
					+ eventId);
		List<EventSecurityUser> memberList = userService
				.getEventSecurityUsersByEventId(eventId,true,false, false,false, false);
		for (EventSecurityUser usr : memberList) {

			// setting the active image count of the uploaded photos
			usr.setImageCount(imageService.getImageCount(
					usr.getEventSecurityUserId(),eventId));

			// setting the active comments count of the user
			usr.setCommentCount(imageService.getCommentCount(
					usr.getEventSecurityUserId(), eventId));

			// setting the active images vote count of the user
			usr.setVoteCount(imageService.getLikeCountBasedOnUser(
					usr.getEventSecurityUserId(),eventId));
			
			
			
		}
		
		List<EventMembersDTO> memeberDetailsList = getMemeberList(memberList);
		return  memeberDetailsList;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getImage")
	public ImageDetailsDTO getImage(@QueryParam("imageId") long imageId,
			@QueryParam("eventId") long eventId,
			@QueryParam("isLoadComment") boolean isLoadComment) throws CoombuWebServiceException {
		log.debug("Entered getImages : parameters eventId: {}", eventId);
		if (!userService.isEventIdAllowedAccess(eventId))
//			throw new EventAccessNotAllowed(
//					"The user does not have access to the event: " + eventId,
//					"");
			throw new CoombuWebServiceException("The user does not have access to the event: "
					+ eventId);
		Image image = imageService.getImage(imageId, isLoadComment);
		ImageDetailsDTO imageDetailsDTO = getImage(image);
		return imageDetailsDTO;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getProfile/{userName}")
	public Response getProfile(@PathParam("userName") String userName)

	{
		log.debug("Entered getProfile - userName: {}", userName);
		UserProfile profile = null;
		String userId = CoombuUtil.getSecurityUserName();
		long securityUserId = CoombuUtil.getSecurityUserId();
		if (userId != null && userName != null && userId.equals(userName)) {
			profile = userService.getUserProfile(securityUserId);

			if (profile == null) {
				return Response.status(Status.NOT_FOUND).build();
			} else {
				return Response.status(Status.OK).entity(profile).build();
			}
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateProfile")
	public UserProfile updateProfile(UserProfile wsProfile)

	{
		log.debug("Entered getProfile - userProfile: {}", wsProfile);
		UserProfile profile = null;
		try {
			long securityUserId = CoombuUtil.getSecurityUserId();
			profile = userService.getUserProfile(securityUserId);
			profile.setFirstName(wsProfile.getFirstName());
			profile.setLastName(wsProfile.getLastName());
			profile.setEmailAddress(wsProfile.getEmailAddress());
			profile.setPhones(wsProfile.getPhones());
			profile.setSecondaryEmailAddress(wsProfile
					.getSecondaryEmailAddress());
			setAddresses(profile.getUserAddresses(),
					wsProfile.getUserAddresses());
			setPhones(profile.getPhones(), wsProfile.getPhones());
			userService.saveUserProfile(profile);
		} catch (Exception e) {
			log.error("Error saving user profile", e);
			profile = null;
		}
		return profile;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/deleteImage/{imageId}")
	public Result deleteImage(@PathParam("imageId") long imageId,
			@QueryParam("eventId") long eventId)
			throws CoombuWebServiceException

	{
		log.debug("Entered deleteImages : parameters imageId: {}, eventId: {}",
				imageId, eventId);
		Result result = new Result();
		try {
			if (!userService.isEventIdAllowedAccess(eventId))
//				throw new EventAccessNotAllowed(
//						"The user does not have access to the event: "
//								+ eventId, "");
				throw new CoombuWebServiceException("The user does not have access to the event: "
						+ eventId);
			long securityUserId = CoombuUtil.getSecurityUserId();
			EventSecurityUser eventSecurityUser = userService
					.getEventSecurityUser(securityUserId, eventId);
			int code = imageService.deleteImage(imageId,
					eventSecurityUser.getEventSecurityUserId());

			if (code == 0) {
				result.setResponseCode(SUCCESS_CODE);
				result.setResponseString(SUCCESS);
			} else if (code == 2) {
				throw new EventAccessNotAllowed(NOT_AUTHORIZED, null);
			}
		} catch (Exception e) {
			throw new CoombuWebServiceException(e.getMessage());
			
		}
		return result;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/addComment")
	public Result addComment(CommentRequest comment)
			throws CoombuWebServiceException {
		Result result = new Result();
		try {
			long eventId = comment.getEventId();
			if (!userService.isEventIdAllowedAccess(comment.getEventId()))
				throw new EventAccessNotAllowed(
						"The user does not have access to the event: "
								+ eventId, "");
			if (comment.getEventSecurityUserId() != this
					.getEventSecurityUserId(eventId))
				throw new EventAccessNotAllowed(
						"The user does not have access to the event: "
								+ eventId, "");
			Image img = imageService.getImage(comment.getImageId(), false);
			if (img.getEvent().getEventId() != eventId) {
				throw new EventAccessNotAllowed(
						"The user does not have access to the image: imageId: "
								+ eventId, "");
			}
			comment.setSecurityUserId(CoombuUtil.getSecurityUserId());
			imageService.addComment(comment);
			result.setResponseCode(SUCCESS_CODE);
			result.setResponseString(SUCCESS);
		} catch (Exception e) {
			log.error("Error adding comment: {}", e);
			// throw new CoombuWebServiceException(e.getMessage());

			result.setResponseCode(1);
			result.setResponseString(e.getMessage());
		}
		return result;
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/addTag")
	public Result addTag(ImageTagRequest tag) throws CoombuWebServiceException {
		Result result = new Result();
		try {
			long eventId = tag.getEventId();
			if (!userService.isEventIdAllowedAccess(tag.getEventId()))
				throw new EventAccessNotAllowed(
						"The user does not have access to the event: "
								+ eventId, "");
			if (tag.getEventSecurityUserId() != this
					.getEventSecurityUserId(eventId))
				throw new EventAccessNotAllowed(
						"The user does not have access to the event: "
								+ eventId, "");
			Image img = imageService.getImage(tag.getImageId(), false);
			if (img.getEvent().getEventId() != eventId) {
				throw new EventAccessNotAllowed(
						"The user does not have access to the image: imageId: "
								+ eventId, "");
			}
			tag.setSecurityUserId(CoombuUtil.getSecurityUserId());
			imageService.addTag(tag);
			result.setResponseCode(SUCCESS_CODE);
			result.setResponseString(SUCCESS);
		} catch (Exception e) {
			log.error("Error adding tag: {}", e);
			 throw new CoombuWebServiceException(e.getMessage());
		}
		return result;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/addLike")
	public Result addLike(LikeRequest like) throws CoombuWebServiceException {
		Result result = new Result();
		try {
			long eventId = like.getEventId();
			if (!userService.isEventIdAllowedAccess(like.getEventId()))
				throw new EventAccessNotAllowed(
						"The user does not have access to the event: "
								+ eventId, "");
			if (like.getEventSecurityUserId() != this
					.getEventSecurityUserId(eventId))
				throw new EventAccessNotAllowed(
						"The user does not have access to the event: "
								+ eventId, "");
			Image img = imageService.getImage(like.getImageId(), false);
			if (img.getEvent().getEventId() != eventId) {
				throw new EventAccessNotAllowed(
						"The user does not have access to the image: imageId: "
								+ eventId, "");
			}
			like.setSecurityUserId(CoombuUtil.getSecurityUserId());
			imageService.addLike(like);
			result.setResponseCode(SUCCESS_CODE);
			result.setResponseString(SUCCESS);
		} catch (Exception e) {
			log.error("Error adding Like: {}", e);
			throw new CoombuWebServiceException(e.getMessage());

		}
		return result;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFlaggedComments")
	public List<RemovalRequest> getFlaggedComments(
			@QueryParam("eventId") long eventId) {

		if (!userService.isUserLead(eventId, CoombuUtil.getSecurityUserId())) {
			throw new EventAccessNotAllowed(
					"The user does not have access to flagged Comments: "
							+ eventId, "");
		}

		return imageService.getFlaggedComments(eventId);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFlaggedImages")
	public List<RemovalRequest> getFlaggedImages(
			@QueryParam("eventId") long eventId) {
		if (!userService.isUserLead(eventId, CoombuUtil.getSecurityUserId())) {
			throw new EventAccessNotAllowed(
					"The user does not have access to flagged Comments: "
							+ eventId, "");
		}

		return imageService.getFlaggedImages(eventId);
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/upload")
	public Image upload(@FormDataParam("image") InputStream istream,
			@FormDataParam("image") FormDataBodyPart body,
			@FormDataParam("eventId") long eventId,
			@FormDataParam("eventDescription") String eventDescription)
			throws CoombuWebServiceException {
		if (!userService.isEventIdAllowedAccess(eventId)) {
//			throw new EventAccessNotAllowed(
//					"The user does not have access to the event: " + eventId,
//					"");
			throw new CoombuWebServiceException("The user does not have access to the event: "
					+ eventId);
		}
		if (body.getContentDisposition() == null) {
			throw new CoombuWebServiceException("Image parameter is missing");
		}
		if (eventDescription == null || eventDescription.length() <= 0) {
			throw new CoombuWebServiceException(
					"eventDescription parameter is missing");
		}
		Image img = null;
		ContentDisposition cd = body.getContentDisposition();
		try {
			long securityUserId = CoombuUtil.getSecurityUserId();
			EventSecurityUser eventSecurityUser = userService
					.getEventSecurityUser(securityUserId, eventId);
			WSUploadedFile wsUploadedFile = new WSUploadedFile(istream, body
					.getMediaType().toString(), cd.getSize(), cd.getFileName());
			img = new Image();
			img.setUploadedFile(wsUploadedFile);
			img.setEvent(eventSecurityUser.getEvent());
			img.setEventSecurityUser(eventSecurityUser);
			img.setEventDescription(eventDescription);
			img.setImageSourceId(IMAGE_SOURCE.MOBILE.id());
			img.setUploadDateTime(new Date());
			img.setImageStatusTypeId(IMAGE_STATUS_TYPE.APPROVED.id());

			imageService.uploadSavePicture(img);

		} catch (Exception e) {
			throw new CoombuWebServiceException(e.getMessage());
		}

		return img;
	}

	/*
	 * New functionalities added by Sajith V.j
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllComments")
	public List<Comment> getAllComments(@QueryParam("eventId") long eventId)
			throws CoombuWebServiceException {
		log.debug("Entered getAllComments : eventId: {}", eventId);
		try {
			if (!userService.isEventIdAllowedAccess(eventId)) {
//				throw new EventAccessNotAllowed(
//						"The user does not have access to the event: "
//								+ eventId, "");
				throw new CoombuWebServiceException("The user does not have access to the event: "
						+ eventId);
			} else {
				long securityUserId = CoombuUtil.getSecurityUserId();
				List<Comment> commentList = imageService.getEventComments(
						securityUserId, eventId);
				return commentList;
			}
		} catch (Exception e) {
			log.error("Error getting Comments: ", e);
			throw new CoombuWebServiceException(e.getMessage());
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searchMembers")
	public Response searchMembers(@QueryParam("userId") long userId) {
		UserProfile profile = null;
		profile = userService.getUserProfile(userId);
		if (profile == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else {
			return Response.status(Status.OK).entity(profile).build();
		}

	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/repostImage")
	public Result repostImage(RepostRequest repostRequest)
			throws CoombuWebServiceException {
		Result result = new Result();
		try {
			long eventId = repostRequest.getEventId();
			if (!userService
					.isUserLead(eventId, CoombuUtil.getSecurityUserId())) {
				throw new EventAccessNotAllowed(
						"The user does not have access to Repost Image: "
								+ eventId, "");
			}
			imageService.repost(repostRequest.getImageId(), eventId,
					IMAGE_STATUS_TYPE.APPROVED.id());
			result.setResponseCode(SUCCESS_CODE);
			result.setResponseString(SUCCESS);
		} catch (Exception e) {
			// e.printStackTrace();
			 throw new CoombuWebServiceException(e.getMessage());
			
		}
		return result;
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/repostComment")
	public Result repostComment(RepostCommentRequest repostRequest)
			throws CoombuWebServiceException {
		Result result = new Result();
		try {
			long eventId = repostRequest.getEventId();
			if (!userService
					.isUserLead(eventId, CoombuUtil.getSecurityUserId())) {
				throw new EventAccessNotAllowed(
						"The user does not have access to Repost Comments: "
								+ eventId, "");
			}
			imageService.repostComment(repostRequest.getCommentId(), eventId,
					COMMENT_STATUS_TYPE.ACTIVE.id());
			result.setResponseCode(SUCCESS_CODE);
			result.setResponseString(SUCCESS);
		} catch (Exception e) {
			 throw new CoombuWebServiceException(e.getMessage());
//			result.setResponseCode(1);
//			result.setResponseString(NOT_AUTHORIZED);
		}
		return result;
	}

	// @POST
	// @Produces({ MediaType.APPLICATION_JSON })
	// @Consumes({ MediaType.APPLICATION_JSON })
	// @Path("/deleteImage")
	// public Result deleteImage(RepostRequest repostRequest)
	// throws CoombuWebServiceException {
	// Result result = new Result();
	// try {
	// long eventId = repostRequest.getEventId();
	// if (!userService
	// .isUserLead(eventId, CoombuUtil.getSecurityUserId())) {
	// throw new EventAccessNotAllowed(
	// "The user does not have access to Repost Image: "
	// + eventId, "");
	// }
	// imageService.repost(repostRequest.getImageId(),
	// eventId,IMAGE_STATUS_TYPE.DELETED.id());
	// result.setResponseCode(SUCCESS_CODE);
	// result.setResponseString(SUCCESS);
	// } catch (Exception e) {
	// // e.printStackTrace();
	// // throw new CoombuWebServiceException(e.getMessage());
	// result.setResponseCode(1);
	// result.setResponseString(NOT_AUTHORIZED);
	// }
	// return result;
	// }

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/deleteComment")
	public Result deleteComment(RepostCommentRequest repostRequest)
			throws CoombuWebServiceException {
		Result result = new Result();
		try {
			long eventId = repostRequest.getEventId();
			if (!userService
					.isUserLead(eventId, CoombuUtil.getSecurityUserId())) {
				throw new EventAccessNotAllowed(
						"The user does not have access to Repost Comments: "
								+ eventId, "");
			}
			imageService.repostComment(repostRequest.getCommentId(), eventId,
					IMAGE_STATUS_TYPE.DELETED.id());
			result.setResponseCode(SUCCESS_CODE);
			result.setResponseString(SUCCESS);
		} catch (Exception e) {
			 throw new CoombuWebServiceException(e.getMessage());
//			result.setResponseCode(1);
//			result.setResponseString(NOT_AUTHORIZED);
		}
		return result;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/flagComment/{commentId}")
	public Result flagComment(@PathParam("commentId") long commentId,
			@QueryParam("eventId") long eventId)
			throws CoombuWebServiceException

	{
		log.debug(
				"Entered flagComment : parameters commentId: {}, eventId: {}",
				commentId, eventId);
		Result result = new Result();
		try {
			if (!userService.isEventIdAllowedAccess(eventId))
				throw new EventAccessNotAllowed(
						"The user does not have access to the event: "
								+ eventId, "");
			long securityUserId = CoombuUtil.getSecurityUserId();
			EventSecurityUser eventSecurityUser = userService
					.getEventSecurityUser(securityUserId, eventId);
			RemovalRequest req = new RemovalRequest(userService.getUserProfile(securityUserId), REQUEST_REMOVAL_TYPE.COMMENT.id(),
					eventId, commentId, 2,
					RESOLUTION_STATUS_TYPE.PENDING.id(), "");
			
			int code = imageService.flagComment(commentId, securityUserId,req);
			if (code == 0) {
				result.setResponseCode(SUCCESS_CODE);
				result.setResponseString(SUCCESS);
			} else if (code == 2) {
				throw new EventAccessNotAllowed(NOT_AUTHORIZED, null);
			}
		} catch (Exception e) {
			throw new CoombuWebServiceException(e.getMessage());
		}
		return result;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/flagImage/{imageId}")
	public Result imageComment(@PathParam("imageId") long imageId,
			@QueryParam("eventId") long eventId)
			throws CoombuWebServiceException

	{
		log.debug("Entered flagImage : parameters commentId: {}, eventId: {}",
				imageId, eventId);
		Result result = new Result();
		try {
			if (!userService.isEventIdAllowedAccess(eventId))
				throw new EventAccessNotAllowed(
						"The user does not have access to the event: "
								+ eventId, "");
			long securityUserId = CoombuUtil.getSecurityUserId();
			EventSecurityUser eventSecurityUser = userService
					.getEventSecurityUser(securityUserId, eventId);
			RemovalRequest req = new RemovalRequest(userService.getUserProfile(securityUserId), REQUEST_REMOVAL_TYPE.IMAGE.id(),
					eventId, imageId, 2,
					RESOLUTION_STATUS_TYPE.PENDING.id(), "");
			
			int code = imageService.flagImage(imageId,securityUserId,req);

			if (code == 0) {
				result.setResponseCode(SUCCESS_CODE);
				result.setResponseString(SUCCESS);
			} else if (code == 2) {
				throw new EventAccessNotAllowed(NOT_AUTHORIZED, null);
			}
		} catch (Exception e) {
			throw new CoombuWebServiceException(e.getMessage());
		}
		return result;
	}

	@GET
	@Produces("multipart/mixed" )
	@Path("/download/{imageId}")
	public MultiPart download(@PathParam("id") long imageId,
			@QueryParam("eventId") long eventId) throws CoombuWebServiceException
	{	
		if (!userService.isEventIdAllowedAccess(eventId))
			throw new EventAccessNotAllowed(
					"The user does not have access to the event: "
							+ eventId, "");
		MultiPart multiPart = new MultiPart();
		try
		{			 
			  multiPart.bodyPart(new BodyPart(MediaType.APPLICATION_XML_TYPE)).bodyPart(
			   new BodyPart(getAttachmentBytes(imageId), MediaType.APPLICATION_OCTET_STREAM_TYPE));
		}
		catch (Exception e) 
		{
			throw new CoombuWebServiceException(e.getMessage());
		}
		
		
		return multiPart;
		
		
	}
	
	/*
	 * New files to create upload images and  
	 * 
	 */
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadImage")
	public Result uploadImage(UploadImage upload)throws CoombuWebServiceException {
		byte []b=new byte[4092];
				
			b=Base64.decode(upload.getImageData().trim());
		Result result = new Result();
		if (!userService.isEventIdAllowedAccess(upload.getEventId())) {
			throw new EventAccessNotAllowed(
					"The user does not have access to the event: " + upload.getEventId(),
					"");
		}
		if (b.length==-1) {
			throw new CoombuWebServiceException("Image parameter is missing");
		}
		if (b.length>Constants.MAX_FILE_SIZE){
			throw new CoombuWebServiceException("Exceeds maximum file size");
		}
		if (upload.getEventDescription() == null || upload.getEventDescription().length() <= 0) {
			throw new CoombuWebServiceException(
					"eventDescription parameter is missing");
		}
		if(upload.getFileName().equals(null)|| upload.getFileName().trim().length() <= 0){
			throw new CoombuWebServiceException(
					"fileName parameter is missing");
		}
		
		if(upload.getFileName().equals(imageService.getOriginallFileName(upload.getFileName()))){
			throw new CoombuWebServiceException(
					"Image already exists");
		}
		Image img = null;
	
		try {
			long securityUserId = CoombuUtil.getSecurityUserId();
			EventSecurityUser eventSecurityUser = userService
					.getEventSecurityUser(securityUserId, upload.getEventId());
			img = new Image();
			
//			img.setUploadedFile(wsUploadedFile);
			img.setFileName(upload.getFileName());
			img.setEvent(eventSecurityUser.getEvent());
			img.setEventSecurityUser(eventSecurityUser);
			img.setEventDescription(upload.getEventDescription());
			img.setImageSourceId(IMAGE_SOURCE.MOBILE.id());
			img.setUploadDateTime(new Date());
			img.setImageStatusTypeId(IMAGE_STATUS_TYPE.APPROVED.id());
			img.setOriginalFileName(upload.getFileName());
			imageService.uploadImageWS(img, b);
			result.setResponseCode(SUCCESS_CODE);
			result.setResponseString(SUCCESS);
			

		} catch (Exception e) {
			throw new CoombuWebServiceException(e.getMessage());
		}

		return result;
	}

	@GET
	@Produces("application/json" )
	@Path("/downloadImage/{imageId}")
	public DownloadImage downloadImage(@PathParam("imageId") long imageId,
			@QueryParam("eventId") long eventId) throws CoombuWebServiceException
	{	
		Result result = new Result();
		
		if (!userService.isEventIdAllowedAccess(eventId))
			throw new EventAccessNotAllowed(
					"The user does not have access to the event: "
						+ eventId, "");
	
		
		try
		{			 
			byte[] imageArray = getAttachmentBytes(imageId);
			String b = new String(Base64.encode(imageArray));
			Image image = imageService.getImage(imageId, false);
			DownloadImage downloadImage = new DownloadImage();
			downloadImage.setFile(b);
			downloadImage.setOriginalName(image.getOriginalFileName());
			
			return downloadImage;
		}
		catch (Exception e) 
		{
			throw new CoombuWebServiceException(e.getMessage());
		}
		
		
		
	}
	
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	@Path("/getTag")
		public List<ImageTagDTO> getTag(@QueryParam("imageId") long imageId,
				@QueryParam("eventId") long eventId) throws CoombuWebServiceException{
		List<ImageTag> imageTag = null;
		if (!userService.isEventIdAllowedAccess(eventId))
			throw new EventAccessNotAllowed(
					"The user does not have access to the event: "
							+ eventId, "");
		try{
			imageTag= imageService.getImageTags(imageId, eventId);
			List<ImageTagDTO> imageTagList = getTagList(imageTag);
			return imageTagList;
		}
		
		catch(Exception e){
			throw new CoombuWebServiceException(e.getMessage());
		}
			
		}
	
	
	private byte[]getAttachmentBytes(long imageId) throws IOException
	{
		Image imageToServe = imageService.getImage(imageId, false);
		StringBuffer fullPath = new StringBuffer(this.imageRoot);
    	fullPath.append(imageToServe.getFilePath())
    	        .append(File.separator)
    	        .append("mobile-"+imageToServe.getFileName());
    	byte[] fileByte = FileUtils.readFileToByteArray(new File(fullPath.toString()));
		return fileByte;
		
	}
	
	
	private void setPhones(Set<Phone> userPhones, Set<Phone> wsPhones) {
		for (Phone uphone : userPhones) {
			boolean found = false;
			for (Phone wsphone : wsPhones) {
				if (wsphone.getPhoneId() == uphone.getPhoneId()) {
					found = true;
					uphone.setPhoneNumber(wsphone.getPhoneNumber());
					uphone.setPhoneTypeId(wsphone.getPhoneTypeId());
				}
			}
			if (!found) {
				userPhones.remove(uphone);
			}

		}
		for (Phone wsphone : wsPhones) {
			boolean found = false;
			for (Phone uphone : userPhones) {
				if (uphone.getPhoneId() == wsphone.getPhoneId()) {
					found = true;
					break;
				}
			}
			if (!found) {
				userPhones.add(wsphone);
			}

		}

	}

	private void setAddresses(Set<UserAddress> userAddresses,
			Set<UserAddress> wsAddresses) {
		if (wsAddresses == null) {
			userAddresses = null;
			return;
		}
		if (userAddresses == null)
			userAddresses = new HashSet<UserAddress>();
		for (UserAddress uadd : userAddresses) {
			boolean found = false;
			for (UserAddress wuadd : wsAddresses) {
				if (uadd.getUserAddressId() == wuadd.getUserAddressId()) {
					found = true;
					uadd.setAddressTypeId(wuadd.getAddressTypeId());
					Address add = uadd.getAddress();
					Address wadd = wuadd.getAddress();
					if (add.getAddressId() == wadd.getAddressId()) {
						add.setAddress1(wadd.getAddress1());
						add.setAddress2(wadd.getAddress2());
						add.setCity(wadd.getCity());
						add.setState(wadd.getState());
						add.setCountryId(wadd.getCountryId());
					}
				}
			}
			if (!found) {
				userAddresses.remove(uadd);
			}

		}
		for (UserAddress wadd : wsAddresses) {
			boolean found = false;
			for (UserAddress uadd : userAddresses) {
				if (uadd.getUserAddressId() == wadd.getAddressTypeId()) {
					found = true;
					break;
				}
			}
			if (!found) {
				userAddresses.add(wadd);
			}
		}

	}

	public long getEventSecurityUserId(long eventId) {
		long securityUserId = CoombuUtil.getSecurityUserId();
		return userService.getEventSecurityUser(securityUserId, eventId)
				.getEventSecurityUserId();

	}
	
	private List<ImageDetailsDTO> getImageDetails(List<Image> imageList){
		List<ImageDetailsDTO> imageDetailsList = new ArrayList<ImageDetailsDTO>();
		if(imageList!=null){
			for (Image image : imageList) {
				ImageDetailsDTO imageDetailsDTO = new ImageDetailsDTO();
				imageDetailsDTO.setImageId(image.getImageId());
				imageDetailsDTO.setFileId(image.getFileId());
				imageDetailsDTO.setFileName(image.getFileName());
				imageDetailsDTO.setEventId(image.getEvent().getEventId());
				imageDetailsDTO.setEventDescription(image.getEventDescription());
				imageDetailsDTO.setFilePath(context.getContextPath()+"/image?id="+image.getFileName()+"&type=mobile");
				imageDetailsDTO.setFileSize(image.getFileSize());
				imageDetailsDTO.setOriginalFileName(image.getOriginalFileName());
				imageDetailsDTO.setCommentCount(image.getCommentCount());
				imageDetailsDTO.setFileTypeId(image.getFileTypeId());
				imageDetailsDTO.setImageHeight(image.getImageHeight());
				imageDetailsDTO.setViewCount(image.getViewCount());
				imageDetailsDTO.setVoteCount(image.getVoteCount());
				imageDetailsDTO.setImageSourceId(image.getImageSourceId());
				imageDetailsDTO.setImageStatusTypeId(image.getImageStatusTypeId());
				imageDetailsDTO.setImageWidth(image.getImageWidth());
				imageDetailsDTO.setTagCount(image.getTagCount());
				imageDetailsDTO.setCommentCount(image.getCommentCount());
				imageDetailsDTO.setUploadDateTime(image.getUploadDateTime());
				imageDetailsList.add(imageDetailsDTO);
			}
		}
	return imageDetailsList;
	}
	
	
	private ImageDetailsDTO getImage(Image image){
		ImageDetailsDTO imageDetailsDTO = new ImageDetailsDTO();
		imageDetailsDTO.setImageId(image.getImageId());
		imageDetailsDTO.setFileId(image.getFileId());
		imageDetailsDTO.setFileName(image.getFileName());
		imageDetailsDTO.setEventId(image.getEvent().getEventId());
		imageDetailsDTO.setEventDescription(image.getEventDescription());
		imageDetailsDTO.setFilePath(context.getContextPath()+"/image?id="+image.getFileName()+"&type=mobile");
		imageDetailsDTO.setFileSize(image.getFileSize());
		imageDetailsDTO.setOriginalFileName(image.getOriginalFileName());
		imageDetailsDTO.setCommentCount(image.getCommentCount());
		imageDetailsDTO.setFileTypeId(image.getFileTypeId());
		imageDetailsDTO.setImageWidth(image.getImageWidth());
		imageDetailsDTO.setImageHeight(image.getImageHeight());
		imageDetailsDTO.setViewCount(image.getViewCount());
		imageDetailsDTO.setVoteCount(image.getVoteCount());
		imageDetailsDTO.setImageSourceId(image.getImageSourceId());
		imageDetailsDTO.setImageStatusTypeId(image.getImageStatusTypeId());
		imageDetailsDTO.setImageWidth(image.getImageWidth());
		imageDetailsDTO.setTagCount(image.getTagCount());
		imageDetailsDTO.setCommentCount(image.getCommentCount());
		imageDetailsDTO.setUploadDateTime(image.getUploadDateTime());
		return imageDetailsDTO;
		
	}
	
	private List<GetEventsDTO> getEventList(List<EventSecurityUser> events){
		List<GetEventsDTO> eventList = new ArrayList<GetEventsDTO>();
		if(events!=null){
			for (EventSecurityUser getEvents : events) {
				GetEventsDTO getEventsDTO = new GetEventsDTO();
				getEventsDTO.setEventSecurityUserId(getEvents.getEventSecurityUserId());
				getEventsDTO.setRoleId((int) getEvents.getRoleId());
			    if(getEvents.getRoleId()==Constants.ROLE.ROLE_ADMIN.id())
			    	getEventsDTO.setRoleName("ROLE_ADMIN");
			    else if(getEvents.getRoleId()==Constants.ROLE.ROLE_REP.id())
			    	getEventsDTO.setRoleName("ROLE_REP");
			    else
			    	getEventsDTO.setRoleName("ROLE_STUDENT");
				getEventsDTO.setEventId(getEvents.getEvent().getEventId());
				getEventsDTO.setEventName(getEvents.getEvent().getEventName());
				getEventsDTO.setEventStatus(getEvents.getEvent().getEventStatus());
				eventList.add(getEventsDTO);
			}
		}
		return eventList;
		
	}
	private List<ImageTagDTO> getTagList(List<ImageTag> imageTag){
		 List<ImageTagDTO> imagTagList = new ArrayList<ImageTagDTO>();
		 if(imageTag!=null){
			 for (ImageTag image : imageTag) {
				 ImageTagDTO dto = new ImageTagDTO();
				 dto.setEventId(image.getEvent().getEventId());
				 dto.setEventSecurityUserId(image.getEventSecurityUser().getEventSecurityUserId());
				 dto.setImageId(image.getImage().getImageId());
				 dto.setSecurityUserId(image.getEventSecurityUser().getSecurityUser().getSecurityUserId());
				 dto.setSecurityUserName(image.getEventSecurityUser().getSecurityUser().getUserName());
				 imagTagList.add(dto); 
				
			}
		 }
		 
		 return imagTagList;
	}
	private List<EventMembersDTO> getMemeberList(List<EventSecurityUser> events){
		 List<EventMembersDTO> imagTagList = new ArrayList<EventMembersDTO>();
		 if(events!=null){
			 for (EventSecurityUser event : events) {
				 EventMembersDTO dto = new EventMembersDTO();
				 dto.setFirstName(event.getSecurityUser().getUserProfile().getFirstName());
				 dto.setLastName(event.getSecurityUser().getUserProfile().getLastName());
				 if(event.getSecurityUser().getUserProfile().getImageFileName()!=null){
					 try {
						dto.setImageData(getProfileImageBytes(event.getSecurityUser().getUserProfile().getImageFileName()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				 }
				dto.setCommentCount(event.getCommentCount());
				dto.setTagCount(event.getTagCount());
				dto.setImageCount(event.getImageCount());
				 imagTagList.add(dto); 
				
			}
		 }
		 
		 return imagTagList;
	}
	
	private String getProfileImageBytes(String imageId) throws IOException
	{
		
		StringBuffer fullPath = new StringBuffer(this.imageRoot);
    	fullPath.append(imageId.substring(0, 2))
    	        .append(File.separator)
    	        .append(imageId.substring(2, 4))
    	         .append(File.separator)
    	         .append(imageId.substring(4, 6))
    	         .append(File.separator)
    	        .append("activity-"+imageId);
    	byte[] fileByte = FileUtils.readFileToByteArray(new File(fullPath.toString()));
    	String b = new String(Base64.encode(fileByte));
    	
    	return b;
		
	}
}
