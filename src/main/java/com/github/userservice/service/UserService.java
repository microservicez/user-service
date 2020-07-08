package com.github.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.userservice.dto.User;
import com.github.userservice.exception.UserNotFoundException;
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
		  return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}
	
	public List<User> getAllUsers() {
		return repository.findAll();
	}
	
	public void removeUser(Integer id) {
		User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		repository.delete(user);
	}
	
	public User modifyUser(Integer id, User user) {
		User data = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		user.setId(data.getId());
		return repository.save(user);
	}
}
