package com.patrickreplogle.bugtracker.services.projects;

import com.patrickreplogle.bugtracker.exceptions.AccessDeniedException;
import com.patrickreplogle.bugtracker.exceptions.ResourceNotFoundException;
import com.patrickreplogle.bugtracker.models.Project;
import com.patrickreplogle.bugtracker.models.User;
import com.patrickreplogle.bugtracker.models.UserRoles;
import com.patrickreplogle.bugtracker.repository.ProjectRepository;
import com.patrickreplogle.bugtracker.repository.UserRepository;
import com.patrickreplogle.bugtracker.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "projectService")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Project> findAll() {
        List<Project> list = new ArrayList<>();

        projectRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    @Override
    public Project findById(long id) throws ResourceNotFoundException {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found."));
    }

    @Transactional
    @Override
    public Project save(Project project) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByName(authentication.getName());

        project = projectRepository.save(project);

        // add user to UserProjects join table to establish many to many relationship in both user and project tables
        if (!project.getUsers().contains(user)) {
            projectRepository.addProjectUser(user.getUserid(), project.getProjectid());
        }

        return findById(project.getProjectid());
    }

    @Transactional
    @Override
    public void delete(long id) throws ResourceNotFoundException, AccessDeniedException {
        Project deletedProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if (!deletedProject.getProjectOwner().getUsername().equalsIgnoreCase(authentication.getName())) {
            throw new AccessDeniedException("User " + authentication.getName() + " does not have permission to delete project id " + id);
        }

        userRepository.deleteUserProject(user.getUserid(), deletedProject.getProjectid());

        projectRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Project update(Project project, long id) {
        Project currentProject = findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!project.getProjectOwner().getUsername().equals(authentication.getName())) {
            throw new AccessDeniedException("User " + authentication.getName() + " does not have permission to update project id " + id);
        }

        if (project.getName() != null) {
            currentProject.setName(project.getName());
        }

        if (project.getDescription() != null) {
            currentProject.setDescription(project.getDescription());
        }

        if (project.getImageurl() != null) {
            currentProject.setImageurl(project.getImageurl());
        }

        if (project.getRepositoryurl() != null) {
            currentProject.setRepositoryurl(project.getRepositoryurl());
        }

        if (project.getWebsiteurl() != null) {
            currentProject.setWebsiteurl(project.getWebsiteurl());
        }

        return projectRepository.save(currentProject);
    }
}
