package com.cts.ems.exception;
 
public class ReviewNotSavedException extends RuntimeException {
    public ReviewNotSavedException(String message) {
        super(message);
    }
}