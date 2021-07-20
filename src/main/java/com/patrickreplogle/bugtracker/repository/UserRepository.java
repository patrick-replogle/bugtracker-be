package com.patrickreplogle.bugtracker.repository;

import com.patrickreplogle.bugtracker.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findByUsernameContainingIgnoreCase(String username);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO userprojects (userid, projectid) VALUES(:userid, :projectid)", nativeQuery = true)
    void addUserProject(long userid, long projectid);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM userprojects WHERE (userid = :userid) AND (projectid = :projectid)", nativeQuery = true)
    void deleteUserProject(long userid, long projectid);

    @Query(value = "SELECT * FROM userprojects WHERE userid = :userid AND projectid = :projectid", nativeQuery = true)
    Object findUserProject(long userid, long projectid);

    @Query(value = "SELECT * FROM users AS u WHERE LOWER(CONCAT(u.firstname, u.lastName)) LIKE CONCAT('%', :name, '%') OR LOWER(u.email) LIKE CONCAT('%', :name, '%') OR LOWER(u.username) LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<User> findUsersBySearchTerm(String name);
}
