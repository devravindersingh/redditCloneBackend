package com.ravinder.rcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ravinder.rcbackend.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
