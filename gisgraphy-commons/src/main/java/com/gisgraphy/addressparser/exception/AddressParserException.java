package com.gisgraphy.addressparser.exception;

public class AddressParserException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -6156172175171988733L;

    public AddressParserException() {
    }

    public AddressParserException(String message) {
	super(message);
    }

    public AddressParserException(Throwable cause) {
	super(cause);
    }

    public AddressParserException(String message, Throwable cause) {
	super(message, cause);
    }

}
