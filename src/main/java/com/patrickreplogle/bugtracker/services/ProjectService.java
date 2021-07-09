package com.patrickreplogle.bugtracker.services;

import com.patrickreplogle.bugtracker.models.Project;

import java.util.List;

public interface ProjectService {
    List<Project> findAll();

    Project findById(long id);

    Project save(Project project);

    void delete(long id);

    Project update(Project project, long id);
}
