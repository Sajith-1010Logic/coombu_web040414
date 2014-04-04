package com.coombu.photobook.webservice;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.coombu.photobook.util.Constants.SUBMENU_TYPE;

@XmlRootElement
public class ImageRequestBean 
{
	@XmlElement
	private SUBMENU_TYPE subMenuType;
	
	@XmlElement
	private long eventId;
	
	@XmlElement 
	private int eventSecurityUserId;

	public SUBMENU_TYPE getSubMenuType() {
		return subMenuType;
	}

	public void setSubMenuType(SUBMENU_TYPE subMenuType) {
		this.subMenuType = subMenuType;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("ImageRequestBean = [EventId: ").append(eventId).append("\n")
		.append("eventSecurityUserId").append(eventSecurityUserId).append("\n")
		.append("subMenuType:").append(subMenuType).append("]\n");
		return buf.toString();
		
	}
	
}
