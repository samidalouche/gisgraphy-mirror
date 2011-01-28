package com.gisgraphy.rest;

public class RestClientException extends RuntimeException {
    private int httpStatus;

    /**
     * 
     */
    public RestClientException() {
	super();
    }

    /**
     * @param message
     * @param cause
     */
    public RestClientException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
     * @param message
     */
    public RestClientException(String message) {
	super(message);
    }

    /**
     * @param cause
     */
    public RestClientException(Throwable cause) {
	super(cause);
    }
    


    public RestClientException(int statusCode, String statusText) {
	this(statusText);
	this.httpStatus = statusCode;
    }

    public int getHttpStatus() {
	return httpStatus;
    }

}
