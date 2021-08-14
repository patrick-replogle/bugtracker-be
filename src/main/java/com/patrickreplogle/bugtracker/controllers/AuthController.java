package com.patrickreplogle.bugtracker.controllers;

import com.patrickreplogle.bugtracker.exceptions.AccessDeniedException;
import com.patrickreplogle.bugtracker.exceptions.ResourceFoundException;
import com.patrickreplogle.bugtracker.models.User;
import com.patrickreplogle.bugtracker.models.UserMinimum;
import com.patrickreplogle.bugtracker.repository.UserRepository;
import com.patrickreplogle.bugtracker.services.users.UserService;
import com.patrickreplogle.bugtracker.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // register a new user record
    @PostMapping(value = "/register",
            consumes = "application/json")
    public ResponseEntity<?> addNewUser(
            @Valid
            @RequestBody
                    UserMinimum newuser)
            throws ResourceFoundException {
        // first check if username or email are already taken
        if (userRepository.findByEmail(newuser.getEmail()) != null || userRepository.findByUsername(newuser.getUsername()) != null) {
            throw new ResourceFoundException("email or username already taken");
        }

        User user = new User();
        user.setUsername(newuser.getUsername());
        user.setEmail(newuser.getEmail());
        user.setImageurl(newuser.getImageurl());
        user.setFirstname(newuser.getFirstname());
        user.setLastname(newuser.getLastname());
        user.setPassword(newuser.getPassword());

        userService.save(user);

        return getToken(newuser);
    }

    // safely login user -> request is made from the server to avoid storing OAUTH secret on the client
    @PostMapping(value = "/login",
            consumes = "application/json")
    public ResponseEntity<?> loginUser(
            @Valid
            @RequestBody
                    UserMinimum user)
            throws ResourceFoundException {

        return getToken(user);
    }

    public ResponseEntity<?> getToken(UserMinimum user) throws HttpStatusCodeException  {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String requestURI = System.getenv(Constants.RUNTIME_ENV).equals("production") ?
                    "https://bugtracker-back-end.herokuapp.com/login" : "http://localhost:2019/login";

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(acceptableMediaTypes);
            headers.setBasicAuth(System.getenv(Constants.OAUTHCLIENTID),
                    System.getenv(Constants.OAUTHCLIENTSECRET));

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type",
                    "password");
            map.add("scope",
                    "read write trust");
            map.add("username",
                    user.getUsername());
            map.add("password",
                    user.getPassword());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,
                    headers);

            String theToken = restTemplate.postForObject(requestURI,
                    request,
                    String.class);

            return new ResponseEntity<>(theToken, HttpStatus.OK);
        } catch (HttpStatusCodeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
