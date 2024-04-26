package com.example.apirift.service.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Object id) {
        super("Resource not found. Id " + id);
    }

    public ResourceNotFoundException(String msg, Object id) {
        super(msg + " " + id);
    }
}
