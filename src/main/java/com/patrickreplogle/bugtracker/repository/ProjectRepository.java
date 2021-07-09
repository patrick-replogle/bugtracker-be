package com.patrickreplogle.bugtracker.repository;

import com.patrickreplogle.bugtracker.models.Project;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO userprojects (userid, projectid) VALUES(:userid, :projectid)", nativeQuery = true)
    void addProjectUser(long userid, long projectid);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM userprojects WHERE (userid = :userid) AND (projectid = :projectid)", nativeQuery = true)
    void deleteProjectUser(long userid, long projectid);

    @Query(value = "SELECT * FROM userprojects WHERE userid = :userid AND projectid = :projectid", nativeQuery = true)
    Object findProjectUser(long userid, long projectid);
}
