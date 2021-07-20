package com.patrickreplogle.bugtracker.controllers;

import com.patrickreplogle.bugtracker.exceptions.ResourceFoundException;
import com.patrickreplogle.bugtracker.models.Comment;
import com.patrickreplogle.bugtracker.services.comments.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // return a single comment by id
    @GetMapping(value = "/comment/{commentid}",
            produces = "application/json")
    public ResponseEntity<?> getCommentById(@PathVariable Long commentid) {
        Comment comment = commentService.findById(commentid);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    // create a new comment
    @PostMapping(value = "/comment",
            consumes = "application/json")
    public ResponseEntity<?> addNewComment (
            @Valid
            @RequestBody
                    Comment newComment)
            throws ResourceFoundException {

        return new ResponseEntity<>(commentService.save(newComment), HttpStatus.CREATED);
    }

    // partially update a comment
    @PatchMapping(value = "/comment/{commentid}",
            consumes = "application/json")
    public ResponseEntity<?> updateComment (
            @RequestBody Comment updateComment, @PathVariable long commentid) {

        return new ResponseEntity<>(commentService.update(updateComment, commentid), HttpStatus.OK);
    }

    // delete a comment record
    @DeleteMapping(value = "/comment/{commentid}")
    public ResponseEntity<?> deleteCommentById(
            @PathVariable
                    long commentid) {
        commentService.delete(commentid);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
