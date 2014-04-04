package com.coombu.photobook.dao;

import com.coombu.photobook.model.CollegeRegistrationRequest;

public interface ICollegeRegistrationRequestDao {

	public abstract void save(CollegeRegistrationRequest transientInstance);

	public abstract void delete(CollegeRegistrationRequest persistentInstance);

	public abstract CollegeRegistrationRequest merge(
			CollegeRegistrationRequest detachedInstance);

	public abstract CollegeRegistrationRequest findById(int id);

}