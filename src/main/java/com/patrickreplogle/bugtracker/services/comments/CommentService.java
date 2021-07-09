package com.patrickreplogle.bugtracker.services.comments;

import com.patrickreplogle.bugtracker.models.Comment;

public interface CommentService {
    Comment save(Comment comment);

    void delete(long id);

    Comment update(Comment comment, long id);

    Comment findById(long id);
}
