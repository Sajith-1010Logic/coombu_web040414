package com.coombu.photobook.service;

import java.util.List;

import com.coombu.photobook.model.CollegeRegistrationRequest;


public interface IRegistrationService 
{

    public Boolean requestRegistration (CollegeRegistrationRequest request);

    public void approveRegistration (CollegeRegistrationRequest registration);

    public List<CollegeRegistrationRequest> getCollegeRegistrationRequests ();

}

