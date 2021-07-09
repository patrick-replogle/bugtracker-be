package com.patrickreplogle.bugtracker;

import com.patrickreplogle.bugtracker.models.*;
import com.patrickreplogle.bugtracker.services.ProjectService;
import com.patrickreplogle.bugtracker.services.RoleService;
import com.patrickreplogle.bugtracker.services.UserService;
import com.patrickreplogle.bugtracker.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    ProjectService projectService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        Role r1 = new Role(Constants.ADMIN_ROLE);
        Role r2 = new Role(Constants.USER_ROLE);

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);

        User user1 = new User("user1", "password", "user1@email.com", "John", "Doe", "coders inc");
        user1.setUserid(0);
        user1.getRoles().add(new UserRoles(user1, r1));
        user1.getRoles().add(new UserRoles(user1, r2));

        User user2 = new User("user2", "password", "user2@email.com", "Jane", "Doe", "bugfixers");
        user2.setUserid(0);
        user2.getRoles().add(new UserRoles(user2, r2));
        user2.getRoles().add(new UserRoles(user2, r1));

        userService.save(user1);
        userService.save(user2);

//        Project p1 = new Project("project1", "description goes here", "", "", "", user1.getUserid());
//        Project p2 = new Project("project2", "description goes here", "", "", "", user2.getUserid());
//
//        p1.setProjectid(0);
//        p2.setProjectid(0);
//
//        projectService.save(p1);
//        projectService.save(p2);
    }
}
