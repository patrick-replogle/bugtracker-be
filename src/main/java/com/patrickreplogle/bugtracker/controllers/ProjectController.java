package com.patrickreplogle.bugtracker.controllers;

import com.patrickreplogle.bugtracker.exceptions.ResourceFoundException;
import com.patrickreplogle.bugtracker.exceptions.ResourceNotFoundException;
import com.patrickreplogle.bugtracker.models.Project;
import com.patrickreplogle.bugtracker.repository.ProjectRepository;
import com.patrickreplogle.bugtracker.services.ProjectService;
import com.patrickreplogle.bugtracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;


    // returns a list of all projects
    @GetMapping(value = "/project",
            produces = "application/json")
    public ResponseEntity<?> listAllUsers() {
        List<Project> myUsers = projectService.findAll();
        return new ResponseEntity<>(myUsers, HttpStatus.OK);
    }

    // return a single project by projectid
    @GetMapping(value = "/project/{projectid}",
            produces = "application/json")
    public ResponseEntity<?> getProjectById(@PathVariable Long projectid) {
        Project project = projectService.findById(projectid);

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    // create a new project
    @PostMapping(value = "/project",
            consumes = "application/json")
    public ResponseEntity<?> addNewProject(
            @Valid
            @RequestBody
                    Project newProject)
            throws ResourceFoundException {

        return new ResponseEntity<>(projectService.save(newProject), HttpStatus.CREATED);
    }

    // partially update a project record
    @PatchMapping(value = "/project/{projectid}",
            consumes = "application/json")
    public ResponseEntity<?> updateProject(
            @RequestBody Project updateProject, @PathVariable long projectid) {
        projectService.update(updateProject, projectid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete a project record
    @DeleteMapping(value = "/project/{projectid}")
    public ResponseEntity<?> deleteProjectById(
            @PathVariable
                    long projectid) {
        projectService.delete(projectid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // add a user to users set
    @PostMapping(value = "project/{projectid}/user/{userid}")
    public ResponseEntity<?> addUserToProject(@PathVariable long userid, @PathVariable long projectid) {
        // check if user & project exist first
        userService.findUserById(userid);
        projectService.findById(projectid);

        Object existingUserProject = projectRepository.findProjectUser(userid, projectid);
        if (existingUserProject != null) {
            throw new ResourceFoundException("User project with Project id " + projectid + " and user id " + userid + " already exists.");
        }
        projectRepository.addProjectUser(userid, projectid);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete a user from users set
    @DeleteMapping (value = "project/{projectid}/user/{userid}")
    public ResponseEntity<?> deleteUserFromProject(@PathVariable long userid, @PathVariable long projectid) {
        // check if user & project exist first
        userService.findUserById(userid);
        projectService.findById(projectid);

        Object existingUserProject = projectRepository.findProjectUser(userid, projectid);
        if (existingUserProject == null) {
            throw new ResourceNotFoundException("User project with Project id " + projectid + " and user id" + userid + " does not exist.");
        }

        projectRepository.deleteProjectUser(userid, projectid);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
