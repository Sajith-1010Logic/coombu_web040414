package com.coombu.photobook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.dao.ICollegeRegistrationRequestDao;
import com.coombu.photobook.dao.ISecurityUserDao;
import com.coombu.photobook.model.CollegeRegistrationRequest;
import com.coombu.photobook.model.SecurityUser;

@Transactional
@Service("collegeService")
public class RegistrationServiceImpl implements IRegistrationService {

	@Autowired
	private ICollegeRegistrationRequestDao registrationDao;
	@Autowired
	private ISecurityUserDao securityUserDao;

	public RegistrationServiceImpl() {
	}

	@Override
	public Boolean requestRegistration(CollegeRegistrationRequest request) {
try{
		if(registrationDao == null){
			return false;}else{
		 registrationDao.save(request);
		 return true;
			}
}
catch(RuntimeException e){
	throw e;
}
	}

	@Override
	public List<CollegeRegistrationRequest> getCollegeRegistrationRequests() {
		return null;
	}

	@Override
	public void approveRegistration(CollegeRegistrationRequest registration) {
	}

	public ICollegeRegistrationRequestDao getRegistrationDao() {
		return registrationDao;
	}

	public void setRegistrationDao(ICollegeRegistrationRequestDao val) {
		this.registrationDao = val;
	}

	public ISecurityUserDao getSecurityUserDao() {
		return securityUserDao;
	}

	public void setSecurityUserDao(ISecurityUserDao val) {
		this.securityUserDao = val;
	}

}
