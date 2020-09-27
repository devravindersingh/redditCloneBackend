package com.ravinder.rcbackend.repository;

import com.ravinder.rcbackend.model.Comment;
import com.ravinder.rcbackend.model.Post;
import com.ravinder.rcbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
