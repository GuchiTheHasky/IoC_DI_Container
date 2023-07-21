package com.husky.container.exception;

public class SAXReaderException extends RuntimeException {

    public SAXReaderException(String message) {
        super(message);
    }

    public SAXReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
