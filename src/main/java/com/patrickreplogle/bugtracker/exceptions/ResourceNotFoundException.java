package com.patrickreplogle.bugtracker.exceptions;

public class ResourceNotFoundException
        extends RuntimeException
{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}