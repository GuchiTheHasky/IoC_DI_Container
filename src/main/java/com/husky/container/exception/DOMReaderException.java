package com.husky.container.exception;

public class DOMReaderException extends RuntimeException {

    public DOMReaderException(String message) {
        super(message);
    }

    public DOMReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
