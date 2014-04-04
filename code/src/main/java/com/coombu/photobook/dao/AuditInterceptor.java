package com.coombu.photobook.dao;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.coombu.photobook.model.IAuditable;
import com.coombu.photobook.model.User;



public class AuditInterceptor extends EmptyInterceptor 
{
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory
	.getLogger(AuditInterceptor.class);

	  /**
     * Called before an object is saved. The interceptor may modify the <tt>state</tt>, which will be used for
     * the SQL <tt>INSERT</tt> and propagated to the persistent object.
     *
     * @return <tt>true</tt> if the user modified the <tt>state</tt> in any way.
     */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] currentState,
			String[] propertyNames, Type[] types) 
	{
		log.debug("checking for auditable class");
		if (entity instanceof IAuditable) 
		{
			log.debug("found Auditable object");
			IAuditable auditTrail = (IAuditable)entity;
			auditTrail.setCreateTimestamp(getNow());			
			auditTrail.setCreatedBy(getUserId());
			setCurrentState(currentState, propertyNames, auditTrail);
			log.debug("set audit informations '{}' for '{}'", auditTrail.toString(), entity.toString());
			return true;
		}
		return false;
	}

	private void setCurrentState(Object[] currentState, String[] propertyNames, IAuditable auditTrail)
	{
		for(int i=0; i < propertyNames.length; i++)
		{
			if("createdBy".equals(propertyNames[i]))
			{
				currentState[i] = auditTrail.getCreatedBy();
			}
			else if ("updatedBy".equals(propertyNames[i]))
			{
				currentState[i] = auditTrail.getUpdatedBy();
			}
			else if("createTimestamp".equals(propertyNames[i]))
			{
				currentState[i] = auditTrail.getCreateTimestamp();
			}
			else if ("updateTimestamp".equals(propertyNames[i]))
			{
				currentState[i] = auditTrail.getUpdateTimestamp();
			}
		}
	}
	
	private Long getUserId() 
	{
		Long userId = -1L;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();	    
	    if (auth != null && auth.getPrincipal() != null)
	    {
	    		User user = (User)auth.getPrincipal();
	    		log.debug("user: ", user);
	    		userId = user.getSecurityUserId();
	    }	
	    return userId;
	 }
	private Timestamp getNow()
	{
		return (new Timestamp(GregorianCalendar.getInstance().getTimeInMillis()));
	}

	 /**
     * Called when an object is detected to be dirty, during a flush. The interceptor may modify the detected
     * <tt>currentState</tt>, which will be propagated to both the database and the persistent object.
     * Note that not all flushes end in actual synchronization with the database, in which case the
     * new <tt>currentState</tt> will be propagated to the object, but not necessarily (immediately) to
     * the database. It is strongly recommended that the interceptor <b>not</b> modify the <tt>previousState</tt>.
     *
     * @return <tt>true</tt> if the user modified the <tt>currentState</tt> in any way.
     */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) 
	{
		if (entity instanceof IAuditable) {
			//AuditTrail auditTrail = getAuditTrail(entity);
			IAuditable auditTrail = (IAuditable)entity;
			auditTrail.setUpdateTimestamp(getNow());
			auditTrail.setUpdatedBy(getUserId());
			setCurrentState(currentState, propertyNames, auditTrail);
			log.debug("set audit informations '{}' for '{}'", auditTrail.toString(), entity.toString());
			return true;
		}
		return false;
	}

	/**
	 * Loops through the array of state objects and returns the AuditTrail
	 * instance
	 * 
	 * @param state
	 * @return
	 */
	/*private AuditTrail getAuditTrail(Object entity) {
		AuditTrail auditTrail = ((Auditable)entity).getAuditTrail();
		if (auditTrail == null)
		{
			auditTrail = new AuditTrail();
			((Auditable)entity).setAuditTrail(auditTrail);
		}
		return auditTrail;
	}*/

}
