package com.yunji.deliveryman.other.exceptions;

/**
 * Thrown when an attempt to bind a socket fails.
 */
public class BindException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3068213223099254705L;

	/**
     * {@inheritDoc}
     */
    public BindException() {
    }

    /**
     * {@inheritDoc}
     */
    public BindException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * {@inheritDoc}
     */
    public BindException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * {@inheritDoc}
     */
    public BindException(Throwable throwable) {
        super(throwable);
    }
}
