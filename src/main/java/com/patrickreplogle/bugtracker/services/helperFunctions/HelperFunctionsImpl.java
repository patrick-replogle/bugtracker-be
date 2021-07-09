package com.patrickreplogle.bugtracker.services.helperFunctions;

import com.patrickreplogle.bugtracker.models.ValidationError;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@Service(value = "helperFunctions")
public class HelperFunctionsImpl implements HelperFunctions{

    @Override
    public List<ValidationError> getConstraintViolation(Throwable cause) {

        // loop until there is no cause or there is a hibernate/jackson exception
        while ((cause != null) && !(cause instanceof org.hibernate.exception.ConstraintViolationException || cause instanceof MethodArgumentNotValidException)) {
            cause = cause.getCause();
        }

        List<ValidationError> listVE = new ArrayList<>();

        if (cause != null)
        {
            if (cause instanceof org.hibernate.exception.ConstraintViolationException)
            {
                org.hibernate.exception.ConstraintViolationException ex = (ConstraintViolationException) cause;
                ValidationError newVe = new ValidationError();
                newVe.setCode(ex.getMessage());
                newVe.setMessage(ex.getConstraintName());
                listVE.add(newVe);
            } else
            {
                if (cause instanceof MethodArgumentNotValidException)
                {
                    MethodArgumentNotValidException ex = (MethodArgumentNotValidException) cause;
                    List<FieldError> fieldErrors = ex.getBindingResult()
                            .getFieldErrors();
                    for (FieldError err : fieldErrors) {
                        ValidationError newVe = new ValidationError();
                        newVe.setCode(err.getField());
                        newVe.setMessage(err.getDefaultMessage());
                        listVE.add(newVe);
                    }
                } else {
                    System.out.println("Error in producing constraint violations exceptions. " +
                            "If we see this in the console a major logic error has occurred in the " +
                            "helperfunction.getConstraintViolation method that we should investigate. " +
                            "Note the application will keep running as this only affects exception reporting!");
                }
            }
        }
        return listVE;
    }
}
