package com.patrickreplogle.bugtracker.controllers;

import com.patrickreplogle.bugtracker.exceptions.ResourceFoundException;
import com.patrickreplogle.bugtracker.models.User;
import com.patrickreplogle.bugtracker.repository.UserRepository;
import com.patrickreplogle.bugtracker.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    // register a new user record
    @PostMapping(value = "/register",
            consumes = "application/json")
    public ResponseEntity<?> addNewUser(
            @Valid
            @RequestBody
                    User newuser)
            throws ResourceFoundException {
        // first check if username or email are already taken
        if (userRepository.findByEmail(newuser.getEmail()) != null || userRepository.findByUsername(newuser.getUsername()) != null) {
            throw new ResourceFoundException("email or username already taken");
        }

        return new ResponseEntity<>(userService.save(newuser), HttpStatus.CREATED);
    }
}
