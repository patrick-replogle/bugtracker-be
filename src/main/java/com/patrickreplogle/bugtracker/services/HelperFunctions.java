package com.patrickreplogle.bugtracker.services;

import com.patrickreplogle.bugtracker.models.ValidationError;

import java.util.List;

public interface HelperFunctions {

    List<ValidationError> getConstraintViolation(Throwable cause);
}
