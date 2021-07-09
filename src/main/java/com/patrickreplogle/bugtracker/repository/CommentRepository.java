package com.patrickreplogle.bugtracker.repository;

import com.patrickreplogle.bugtracker.models.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
