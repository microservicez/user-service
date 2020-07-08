package com.github.userservice.exception;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(Integer id) {
		super("user with user id: "+ id +" not found");
	}
}
