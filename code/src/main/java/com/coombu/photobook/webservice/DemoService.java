package com.coombu.photobook.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Media;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.service.IUserService;
import com.coombu.photobook.util.CoombuUtil;

@Component
@Path("/Demo")
public class DemoService {
	@Autowired
	IUserService userService;
   
    UserProfile profile;
    
    List<UserProfile> userList;
    
	@GET
	@Path("/StudentList")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserProfile> getUsers(){
		
		System.out.println("<---------Code is coming to get users-------->");
		userList= new ArrayList<>();
		long securityUserId = CoombuUtil.getSecurityUserId();
		profile = userService.getUserProfile(securityUserId);
		userList.add(profile);
		return userList;
		
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public List<UserProfile> getUserList() {
		return userList;
	}

	public void setUserList(List<UserProfile> userList) {
		this.userList = userList;
	}
	

}
