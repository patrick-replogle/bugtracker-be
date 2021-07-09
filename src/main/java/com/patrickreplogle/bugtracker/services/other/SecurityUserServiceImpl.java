package com.patrickreplogle.bugtracker.services.other;

import com.patrickreplogle.bugtracker.exceptions.ResourceNotFoundException;
import com.patrickreplogle.bugtracker.models.User;
import com.patrickreplogle.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "securityUserService")
public class SecurityUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username.toLowerCase());

        if (user == null) {
            throw new ResourceNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.getAuthority());
    }
}
