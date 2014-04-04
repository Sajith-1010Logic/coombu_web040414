package com.coombu.photobook.dao;

import com.coombu.photobook.model.SecurityQuestionAnswer;

public interface ISecurityQuestionAnswerDao {

	public abstract void save(SecurityQuestionAnswer transientInstance);

	public abstract void delete(SecurityQuestionAnswer persistentInstance);

	public abstract SecurityQuestionAnswer merge(
			SecurityQuestionAnswer detachedInstance);

	public abstract SecurityQuestionAnswer findById(int id);

}