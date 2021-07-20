package com.patrickreplogle.bugtracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wakeup")
public class WakeupController {

    // wake up heroku
    @GetMapping(value = "/heroku", produces = "application/json")
    public ResponseEntity<?> wakeupServer() {

        return new ResponseEntity<>("It's alive!", HttpStatus.OK);
    }
}
