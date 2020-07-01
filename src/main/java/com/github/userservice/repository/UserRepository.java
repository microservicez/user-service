package com.github.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.userservice.dto.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
