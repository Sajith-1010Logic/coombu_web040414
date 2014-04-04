package com.coombu.photobook.dao.exception;

public class DuplicateVoteDaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * initializes this exception with the given throwable
     * 
     * @param t
     */
    public DuplicateVoteDaoException(Throwable t) {
        super(t);
    }

    /**
     * initializes this exception with the given detail message
     * 
     * @param message
     */
    public DuplicateVoteDaoException(String message) {
        super(message);
    }

    /**
     * initializes this exception with the given detail message and throwable
     * 
     * @param message
     * @param t
     */
    public DuplicateVoteDaoException(String message, Throwable t) {
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
    public DuplicateVoteDaoException(String exceptionName, String className,
            Throwable t) {
        super(exceptionName + " occurred in " + className + ".  Error msg is: "
                + t.getMessage(), t);
    }
}
