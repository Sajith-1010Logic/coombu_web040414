package com.coombu.photobook.webservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class EventAccessNotAllowed extends WebApplicationException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * initializes this exception with the given throwable
     * 
     * @param t
     */
    public EventAccessNotAllowed(Throwable t) {
        super(t);
    }

    public EventAccessNotAllowed(String message, String realm)
    {
        super(Response.status(Status.UNAUTHORIZED).entity(message).build());
    }

}
