package com.yunji.deliveryman.other.exceptions;

/**
 * Thrown when a required object is not found.
 */
public class ObjectNotFoundException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4833692526655844105L;

	/**
     * {@inheritDoc}
     */
    public ObjectNotFoundException() {
    }

    /**
     * {@inheritDoc}
     */
    public ObjectNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * {@inheritDoc}
     */
    public ObjectNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * {@inheritDoc}
     */
    public ObjectNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
