package com.patrickreplogle.bugtracker.services;

import com.patrickreplogle.bugtracker.models.Project;
import com.patrickreplogle.bugtracker.models.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    List<User> findByNameContaining(String username);

    User findByName(String username);

    User findUserByEmail(String email);

    User findUserById(long id);

    User save(User user);

    void delete(long id);

    User update(User user, long id);
}
