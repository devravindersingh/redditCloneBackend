package com.ravinder.rcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ravinder.rcbackend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
