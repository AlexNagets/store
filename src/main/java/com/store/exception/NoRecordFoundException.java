package com.store.exception;

public final class NoRecordFoundException extends RuntimeException {
    public NoRecordFoundException(final String message) {
        super(message);
    }
}
