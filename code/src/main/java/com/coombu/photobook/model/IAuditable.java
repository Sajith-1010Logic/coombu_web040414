package com.coombu.photobook.model;


import java.sql.Timestamp;
import java.util.Date;


public interface IAuditable 
{
	public Date getCreateTimestamp();

	public void setCreateTimestamp(Timestamp createTimestamp);
	
	public Long getCreatedBy();
	
	public void setCreatedBy(Long securityUserId);
	
	public Timestamp getUpdateTimestamp();
	
	public void setUpdateTimestamp(Timestamp updateTimestamp);
	
	public Long getUpdatedBy();
	
	public void setUpdatedBy(Long securityUserId);

}

