package com.patrickreplogle.bugtracker.models;

public class ValidationError {
    // === fields ===
    private String Code;

    private String message;

    // === getters/setters ===
    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // === override methods ===
    @Override
    public String toString() {
        return "ValidationError{" + "Code='" + Code + '\'' + ", message='" + message + '\'' + '}';
    }
}
