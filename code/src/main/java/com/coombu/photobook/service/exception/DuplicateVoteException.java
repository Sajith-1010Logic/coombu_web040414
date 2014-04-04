package com.coombu.photobook.service.exception;

public class DuplicateVoteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * initializes this exception with the given throwable
     * 
     * @param t
     */
    public DuplicateVoteException(Throwable t) {
        super(t);
    }

    /**
     * initializes this exception with the given detail message
     * 
     * @param message
     */
    public DuplicateVoteException(String message) {
        super(message);
    }

    /**
     * initializes this exception with the given detail message and throwable
     * 
     * @param message
     * @param t
     */
    public DuplicateVoteException(String message, Throwable t) {
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
    public DuplicateVoteException(String exceptionName, String className,
            Throwable t) {
        super(exceptionName + " occurred in " + className + ".  Error msg is: "
                + t.getMessage(), t);
    }
}
