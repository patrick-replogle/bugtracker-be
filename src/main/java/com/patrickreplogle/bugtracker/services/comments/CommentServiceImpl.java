package com.patrickreplogle.bugtracker.services.comments;

import com.patrickreplogle.bugtracker.exceptions.AccessDeniedException;
import com.patrickreplogle.bugtracker.exceptions.ResourceNotFoundException;
import com.patrickreplogle.bugtracker.models.Comment;
import com.patrickreplogle.bugtracker.models.User;
import com.patrickreplogle.bugtracker.repository.CommentRepository;
import com.patrickreplogle.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(value = "commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Comment findById(long id) throws ResourceNotFoundException {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found."));
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void delete(long id) {
        Comment commentToDelete = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket id " + id + " not found."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if (user.getUserid() != commentToDelete.getCommentOwner().getUserid()) {
            throw new AccessDeniedException("User " + authentication.getName() + " does not have permission to delete comment id " + id);
        }

        commentRepository.deleteById(id);
    }

    @Override
    public Comment update(Comment comment, long id) {
        Comment currentComment = findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if (user.getUserid() != currentComment.getCommentOwner().getUserid()) {
            throw new AccessDeniedException("User " + authentication.getName() + " does not have permission to update comment id " + id);
        }

        if (comment.getComment() != null) {
            currentComment.setComment(comment.getComment());
        }

        return commentRepository.save(currentComment);
    }
}
