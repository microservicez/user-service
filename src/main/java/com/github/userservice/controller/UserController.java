package com.github.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.userservice.dto.User;
import com.github.userservice.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserService service;
	
	@Autowired
	public UserController(UserService service) {
		this.service = service;
	}
	
	@GetMapping("/ping")
	public String checkServer() {
		return "pong";
	}
	
	@PostMapping()
	public ResponseEntity<Object> addUser(@RequestBody User user) { 	
		User data = service.addUser(user);
		return ResponseEntity.status(201).body(data);
	}
	
	@GetMapping()
	public ResponseEntity<Object> getUser() {
		List<User> users = service.getAllUsers();
		return CollectionUtils.isEmpty(users) ?
				ResponseEntity.ok().build() :
				ResponseEntity.ok().body(users);
	}
	
	@GetMapping("/{user_id}")
	public ResponseEntity<Object> getUser(@PathVariable("user_id") Integer userId) {
		User user = service.getUser(userId);
		return user != null ? ResponseEntity.status(200).body(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@DeleteMapping("{user_id}")
	public ResponseEntity<Object> removeUser(@PathVariable("user_id") Integer userId) {
		if(service.removeUser(userId)) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@PutMapping("{user_id}")
	public ResponseEntity<Object> modifyUser(@PathVariable("user_id") Integer user_id, @RequestBody User user) {
		User data = service.modifyUser(user_id, user);
		if (data == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(data);
	}
}
