package com.coombu.photobook.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.model.lookup.AddressType;
import com.coombu.photobook.model.lookup.BookStatusType;
import com.coombu.photobook.model.lookup.CommentStatusType;
import com.coombu.photobook.model.lookup.Country;
import com.coombu.photobook.model.lookup.EventTypeTable;
import com.coombu.photobook.model.lookup.FileType;
import com.coombu.photobook.model.lookup.ImageSource;
import com.coombu.photobook.model.lookup.ImageStatusType;
import com.coombu.photobook.model.lookup.PhoneType;
import com.coombu.photobook.model.lookup.Privilege;
import com.coombu.photobook.model.lookup.RequestReason;
import com.coombu.photobook.model.lookup.RequestRemovalType;
import com.coombu.photobook.model.lookup.ResolutionStatus;
import com.coombu.photobook.model.lookup.Role;
import com.coombu.photobook.model.lookup.UserStatus;
import com.coombu.photobook.model.lookup.VoteType;

@Service("referenceData")
@Repository
public class ReferenceData implements IReferenceData 
{
	private static final Logger log = LoggerFactory.getLogger(ReferenceData.class);

	private static List <AddressType>addressTypelist;
	private static List <BookStatusType>bookStsatusTypeList;
	private static List <CommentStatusType>commentStatusTypeList;
	private static List <Country>countryList;
	private static List <EventTypeTable>eventTypeTableList;
	private static List <FileType>fileTypeList;
	private static List <ImageSource>imageSourceList;
	private static List <ImageStatusType>imageStatusTypeList;
	private static List <PhoneType>phoneTypeList;
	private static List <Privilege>privilegeList;
	private static List <RequestReason>requestReasonList;
	private static List <RequestRemovalType>requestRemovalTypeList;
	private static List <ResolutionStatus>ResolutionStatusList;
	private static List <Role>roleList;
	private static List <UserStatus>userStatusList;
	private static List <VoteType>voteTypeList;

	
	@Autowired
	private SessionFactory sessionFactory;
	
		
	public ReferenceData()
	{
		log.info("ReferenceData object created");
	}

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public static List<AddressType> getAddressTypelist() {
		return addressTypelist;
	}
	public static List<BookStatusType> getBookStsatusTypeList() {
		return bookStsatusTypeList;
	}
	public static List<CommentStatusType> getCommentStatusTypeList() {
		return commentStatusTypeList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Country> getCountryList() {
		if (countryList == null) {			
			 countryList = (List<Country>)getSession().createQuery("FROM Country").list();
		}		
		return countryList;
	}
	public static List<EventTypeTable> getEventTypeTableList() {
		return eventTypeTableList;
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional	
	public List<FileType> getFileTypeList() {
		if (fileTypeList == null) {			
			fileTypeList = (List<FileType>)getSession().createQuery("FROM FileType").list();
		}		
		return fileTypeList;
	}
	public static List<ImageSource> getImageSourceList() {
		return imageSourceList;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<ImageStatusType> getImageStatusTypeList() {
		if ( imageStatusTypeList == null)
			imageStatusTypeList = (List<ImageStatusType>)getSession().createQuery("FROM ImageStatusType").list();
		return imageStatusTypeList;
	}
	
	public static List<PhoneType> getPhoneTypeList() {
		return phoneTypeList;
	}
	public static List<Privilege> getPrivilegeList() {
		return privilegeList;
	}
	@Transactional
	@SuppressWarnings("unchecked")
	public List<RequestReason> getRequestReasonList() 
	{
		if ( requestReasonList == null)
			requestReasonList = (List<RequestReason>)getSession().createQuery("FROM RequestReason").list();
		return requestReasonList;
	}
	public static List<RequestRemovalType> getRequestRemovalTypeList() {
		return requestRemovalTypeList;
	}
	public static List<ResolutionStatus> getResolutionStatusList() {
		return ResolutionStatusList;
	}
	@SuppressWarnings("unchecked")
	public List<Role> getRoleList() {
		if ( roleList == null)
			roleList = (List<Role>)getSession().createQuery("FROM Role").list();
		
		return roleList;
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<UserStatus> getUserStatusList() 
	{
		if ( userStatusList == null)
		{
			userStatusList  = (List<UserStatus>)getSession().createQuery("FROM UserStatus").list();
		}
		return userStatusList;
	}
	public String getRoleName(short id)
	{
		for(int i=0; i< getRoleList().size(); i++)
		{
			Role role = getRoleList().get(i);
			if ( role.getRoleId() == id)
				return role.getRoleName();
		}
		return "";
	}
	public static List<VoteType> getVoteTypeList() {
		return voteTypeList;
	}
	
	@Override
	@Transactional
	public void init() 
	{
		log .info("Initializaing Refrence Data");		
		getRoleList();
		log.info("init Role List - {} elements loaded", roleList.size());
		
		getUserStatusList();
		log.info("init User Status Type List - {} elements loaded", userStatusList.size());		
		
		getCountryList();
		log.info("init Country List - {} elements loaded", countryList.size());		
				
		getFileTypeList();
		log.info("init File Type List - {} elements loaded", fileTypeList.size());		

		getImageStatusTypeList();
		log.info("init Image Status Type List - {} elements loaded", imageStatusTypeList.size());
		
		getRequestReasonList();
		log.info("init Request Reason List - {} elements loaded", requestReasonList.size());

	}

	public String getReason(int requestReasonId) {
		for (RequestReason reason :getRequestReasonList())
		{
			if ( reason.getRequestReasonId() == requestReasonId)
			{
				return reason.getRequestReasonDescription();
			}
		}
		return "";		
	}
}
