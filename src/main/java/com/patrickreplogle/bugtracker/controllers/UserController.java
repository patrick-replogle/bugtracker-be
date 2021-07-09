package com.patrickreplogle.bugtracker.controllers;

import com.patrickreplogle.bugtracker.exceptions.ResourceFoundException;
import com.patrickreplogle.bugtracker.exceptions.ResourceNotFoundException;
import com.patrickreplogle.bugtracker.models.User;
import com.patrickreplogle.bugtracker.repository.UserRepository;
import com.patrickreplogle.bugtracker.services.ProjectService;
import com.patrickreplogle.bugtracker.services.UserService;
import com.patrickreplogle.bugtracker.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    // returns a list of all users
    @GetMapping(value = "/users",
            produces = "application/json")
    public ResponseEntity<?> listAllUsers() {
        List<User> myUsers = userService.findAll();
        return new ResponseEntity<>(myUsers, HttpStatus.OK);
    }

    // returns a single user based on a user id
    @GetMapping(value = "/user/{userId}",
            produces = "application/json")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        User user = userService.findUserById(userId);

        return new ResponseEntity<>(user,
                HttpStatus.OK);
    }

    // return a user object based on a given username
    @GetMapping(value = "/user/name/{userName}",
            produces = "application/json")
    public ResponseEntity<?> getUserByName(
            @PathVariable
                    String userName) {
        User user = userService.findByName(userName);
        return new ResponseEntity<>(user,
                HttpStatus.OK);
    }

    // returns a list of users whose username contains the given substring
    @GetMapping(value = "/user/name/like/{userName}",
            produces = "application/json")
    public ResponseEntity<?> getUserLikeName(
            @PathVariable
                    String userName) {
        List<User> user = userService.findByNameContaining(userName);
        return new ResponseEntity<>(user,
                HttpStatus.OK);
    }

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

   // partially update a user record
    @PatchMapping(value = "/user/{id}",
            consumes = "application/json")
    public ResponseEntity<?> updateUser(
            @RequestBody
                    User updateUser,
            @PathVariable
                    long id) {
        userService.update(updateUser,
                id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete a user record
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUserById(
            @PathVariable
                    long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // add a project to user set
    @PostMapping(value = "user/{userid}/project/{projectid}")
    public ResponseEntity<?> addUserProject(@PathVariable long userid, @PathVariable long projectid) {
        // check if user & project exist first
        userService.findUserById(userid);
        projectService.findById(projectid);

        Object existingUserProject = userRepository.findUserProject(userid, projectid);
        if (existingUserProject != null) {
            throw new ResourceFoundException("User project with Project id " + projectid + " and user id " + userid + " already exists.");
        }
        userRepository.addUserProject(userid, projectid);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete a project from user set
    @DeleteMapping (value = "user/{userid}/project/{projectid}")
    public ResponseEntity<?> deleteUserProject(@PathVariable long userid, @PathVariable long projectid) {
        // check if user & project exist first
        userService.findUserById(userid);
        projectService.findById(projectid);

        Object existingUserProject = userRepository.findUserProject(userid, projectid);
        if (existingUserProject == null) {
            throw new ResourceNotFoundException("User project with Project id " + projectid + " and user id" + userid + " does not exist.");
        }

        userRepository.deleteUserProject(userid, projectid);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
