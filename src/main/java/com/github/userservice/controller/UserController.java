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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @ApiOperation(value = "Adds User")
    @PostMapping()
    public ResponseEntity<Object> addUser(@RequestBody User user) {
        User data = service.addUser(user);
        return ResponseEntity.status(201).body(data);
    }

    @ApiOperation(value = "Find all the users")
    @GetMapping()
    public ResponseEntity<Object> getUsers() {
        List<User> users = service.getAllUsers();
        return CollectionUtils.isEmpty(users) ? ResponseEntity.ok().build() : ResponseEntity.ok().body(users);
    }

    @ApiOperation(value = "Find the user on the basis of user_id", notes = "Provide a user id to get the user details")
    @GetMapping("/{user_id}")
    public ResponseEntity<Object> getUser(
            @ApiParam(value = "id of the user to be retrieved", required = true) @PathVariable("user_id") Integer userId) {
        User user = service.getUser(userId);
        return ResponseEntity.status(200).body(user);
    }

    @ApiOperation(value = "remove a user on the basis of user_id", notes = "deletes the user from the system with the given user_id")
    @DeleteMapping("{user_id}")
    public ResponseEntity<Object> removeUser(
            @ApiParam(value = "id of the user to be removed", required = true) @PathVariable("user_id") Integer userId) {
        service.removeUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "modify a user")
    @PutMapping("{user_id}")
    public ResponseEntity<Object> modifyUser(
            @ApiParam(value = "id of the user to be modified", required = true) @PathVariable("user_id") Integer userId,
            @RequestBody User user) {
        User data = service.modifyUser(userId, user);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
