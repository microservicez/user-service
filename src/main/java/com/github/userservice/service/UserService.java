package com.github.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.userservice.dto.User;
import com.github.userservice.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository repository;
	
	@Autowired
	public UserService(UserRepository repository) {
		this.repository = repository;
	}
	
	public User addUser(User user) {
		return repository.save(user);
	}
	
	public User getUser(Integer id) {
		  return repository.findById(id).orElse(null);
	}
	
	public List<User> getAllUsers() {
		return repository.findAll();
	}
	
	public boolean removeUser(Integer id) {
		Optional<User> user = repository.findById(id);
		if (user.isPresent()) { 
			repository.delete(user.get());
			return true;
		}
		return false;
	}
	
	public User modifyUser(Integer id, User user) {
		Optional<User> data = repository.findById(id);
		if (data.isPresent()) {
			user.setId(id);
			return repository.save(user);
		}
		return null;
	}
}
