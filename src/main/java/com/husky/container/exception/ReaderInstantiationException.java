package com.husky.container.exception;

public class ReaderInstantiationException extends RuntimeException {

    public ReaderInstantiationException(String message) {
        super(message);
    }

    public ReaderInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
