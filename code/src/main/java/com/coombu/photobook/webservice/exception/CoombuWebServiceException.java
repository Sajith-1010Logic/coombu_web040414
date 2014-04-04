package com.coombu.photobook.webservice.exception;

public class CoombuWebServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * initializes this exception with the given throwable
     * 
     * @param t
     */
    public CoombuWebServiceException(Throwable t) {
        super(t);
    }

    /**
     * initializes this exception with the given detail message
     * 
     * @param message
     */
    public CoombuWebServiceException(String message) {
        super(message);
    }

    /**
     * initializes this exception with the given detail message and throwable
     * 
     * @param message
     * @param t
     */
    public CoombuWebServiceException(String message, Throwable t) {
        super(message, t);
    }

    /**
     * Initializes this exception with the given exceptionName, className and
     * throwable
     * 
     * @param exceptionName
     * @param className
     * @param t
     */
    public CoombuWebServiceException(String exceptionName, String className,
            Throwable t) {
        super(exceptionName + " occurred in " + className + ".  Error msg is: "
                + t.getMessage(), t);
    }
	
}
