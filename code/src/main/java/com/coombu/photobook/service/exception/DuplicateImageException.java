package com.coombu.photobook.service.exception;

	public class DuplicateImageException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	    /**
	     * initializes this exception with the given throwable
	     * 
	     * @param t
	     */
	    public DuplicateImageException(Throwable t) {
	        super(t);
	    }

	    /**
	     * initializes this exception with the given detail message
	     * 
	     * @param message
	     */
	    public DuplicateImageException(String message) {
	        super(message);
	    }

	    /**
	     * initializes this exception with the given detail message and throwable
	     * 
	     * @param message
	     * @param t
	     */
	    public DuplicateImageException(String message, Throwable t) {
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
	    public DuplicateImageException(String exceptionName, String className,
	            Throwable t) {
	        super(exceptionName + " occurred in " + className + ".  Error msg is: "
	                + t.getMessage(), t);
	    }
	}


