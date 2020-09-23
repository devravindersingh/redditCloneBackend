package com.ravinder.rcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ravinder.rcbackend.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
