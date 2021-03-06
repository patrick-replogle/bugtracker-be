package com.patrickreplogle.bugtracker.services.users;

import com.patrickreplogle.bugtracker.exceptions.ResourceNotFoundException;
import com.patrickreplogle.bugtracker.exceptions.AccessDeniedException;
import com.patrickreplogle.bugtracker.models.*;
import com.patrickreplogle.bugtracker.repository.UserRepository;
import com.patrickreplogle.bugtracker.services.projects.ProjectService;
import com.patrickreplogle.bugtracker.services.roles.RoleService;
import com.patrickreplogle.bugtracker.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    CloudinaryUploader cloudinaryUploader;

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();

        userRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    @Override
    public List<User> findByNameContaining(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    @Override
    public User findByName(String username) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username.toLowerCase());

        if (user == null) {
            throw new ResourceNotFoundException("User " + username + " not found.");
        }

        return user;
    }

    @Override
    public User findUserByEmail(String email) throws ResourceNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("User email" + email + " not found.");
        }

        return user;
    }

    @Override
    public List<User> findUsersBySearchTerm(String name) {
        return userRepository.findUsersBySearchTerm(name);
    }

    @Override
    public User findUserById(long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found."));
    }

    @Transactional
    @Override
    public User save(User user) {
            // assign a new user a "user" role if they haven't been assigned one already
            if (user.getRoles().size() == 0) {
                Role addRole = roleService.findByName(Constants.USER_ROLE);
                user.getRoles().add(new UserRoles(user, addRole));
            }

            // upload image to cloudinary
            if (user.getImageurl() != null && !user.getImageurl().startsWith("http")) {
                user.setImageurl(cloudinaryUploader.addNewImage(user.getImageurl()));
            }

            return userRepository.save(user);
    }

    @Transactional
    @Override
    public void delete(long id) throws ResourceNotFoundException, AccessDeniedException  {
       User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userToDelete.getUsername() != authentication.getName()) {
            throw new AccessDeniedException("User " + authentication.getName() + " does not have permission to delete user id " + userToDelete.getUserid());
        }

        // delete image from cloudinary
        if (userToDelete.getImageurl() != null) {
            cloudinaryUploader.removeImage(userToDelete.getImageurl());
        }

        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User update(User user, long id) throws ResourceNotFoundException, AccessDeniedException  {
        User currentUser = findUserById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!currentUser.getUsername().equals(authentication.getName())) {
            throw new AccessDeniedException("User " + authentication.getName() + " does not have permission to update user id " + id);
        }

        if (user.getUsername() != null) {
            currentUser.setUsername(user.getUsername()
                    .toLowerCase());
        }

        if (user.getPassword() != null) {
            currentUser.setPasswordNoEncrypt(user.getPassword());
        }

        if (user.getFirstname() != null) {
            currentUser.setFirstname(user.getFirstname());
        }

        if (user.getLastname() != null) {
            currentUser.setLastname(user.getLastname());
        }

        if (user.getCompany() != null) {
            currentUser.setCompany(user.getCompany());
        }

        if (user.getImageurl() != null && !user.getImageurl().startsWith("http")) {
            String url = currentUser.getImageurl();
            if (url == null) {
                currentUser.setImageurl(cloudinaryUploader.addNewImage(user.getImageurl()));
            } else {
                currentUser.setImageurl(cloudinaryUploader.updateImage(user.getImageurl(), url));
            }
        }

        return userRepository.save(currentUser);
    }
}
