package com.husky.container.exception;

public class XMLReaderException extends RuntimeException {

    public XMLReaderException(String message) {
        super(message);
    }

    public XMLReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
