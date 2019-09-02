package org.lxy.utils;

public class TryLockException extends RuntimeException {

    public TryLockException(){

    }

    public TryLockException(String message) {
        super(message);
    }
}
