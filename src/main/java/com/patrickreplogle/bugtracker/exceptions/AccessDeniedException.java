package com.patrickreplogle.bugtracker.exceptions;

public class AccessDeniedException
        extends RuntimeException
{
    public AccessDeniedException(String message) {
        super(message);
    }
}
