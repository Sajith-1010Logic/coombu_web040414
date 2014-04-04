package com.coombu.photobook.service.exception;

	public class DuplicateTagException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	    /**
	     * initializes this exception with the given throwable
	     * 
	     * @param t
	     */
	    public DuplicateTagException(Throwable t) {
	        super(t);
	    }

	    /**
	     * initializes this exception with the given detail message
	     * 
	     * @param message
	     */
	    public DuplicateTagException(String message) {
	        super(message);
	    }

	    /**
	     * initializes this exception with the given detail message and throwable
	     * 
	     * @param message
	     * @param t
	     */
	    public DuplicateTagException(String message, Throwable t) {
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
	    public DuplicateTagException(String exceptionName, String className,
	            Throwable t) {
	        super(exceptionName + " occurred in " + className + ".  Error msg is: "
	                + t.getMessage(), t);
	    }
	}


