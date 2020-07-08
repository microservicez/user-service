package com.github.userservice.units.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.userservice.dto.User;
import com.github.userservice.exception.UserNotFoundException;
import com.github.userservice.repository.UserRepository;
import com.github.userservice.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private UserRepository repository;
	
	private UserService service;
	
	@BeforeEach
	public void test() {
		service = new UserService(repository);
	}
	
	@Test
	public void addUser_shouldReturnUser() {
		given(repository.save(Mockito.any(User.class))).willReturn(new User());
		User user = service.addUser(new User());
		assertThat(user).isNotNull();
	}
	
	@Test
	public void getUser_returnsUser() {
		given(repository.findById(Mockito.anyInt())).willReturn(Optional.of(new User()));
		User user = service.getUser(Mockito.anyInt());
		assertThat(user).isNotNull();
	}
	
	@Test
	public void getUser_throwsUserNotFoundExcpetion() {
		given(repository.findById(Mockito.anyInt())).willThrow(UserNotFoundException.class);
		assertThrows(UserNotFoundException.class, () -> service.getUser(Mockito.anyInt()));
	}

	@Test
	public void getAllUsers_returnsAllUsers() {
		User user = new User();
		user.setId(1);
		user.setName("John");
		List<User> users = List.of(user);
		given(repository.findAll()).willReturn(users);
		List<User> allUsers = service.getAllUsers();
		
		assertThat(allUsers.size()).isEqualTo(1);
		assertThat(allUsers.get(0).getId()).isEqualTo(1);
		assertThat(allUsers.get(0).getName()).isEqualTo("John");
	}
	
	@Test
	public void modifyUser_modifiesUser() {
		User oldUser = new User(1, "John Doe");
		User newUser = new User(1, "Jane Doe");
		
		given(repository.findById(1)).willReturn(Optional.of(oldUser));
		given(repository.save(newUser)).willReturn(newUser);
		
		User modifiedUser = service.modifyUser(1, newUser);
		assertThat(modifiedUser.getId()).isEqualTo(newUser.getId());
		assertThat(modifiedUser.getName()).isEqualTo(newUser.getName());
	}
	
	@Test
	public void modifyUser_throwsUserNotFoundException() {
		User newUser = new User(1, "Jane Doe");
		
		given(repository.findById(Mockito.anyInt())).willThrow(UserNotFoundException.class);
		assertThrows(UserNotFoundException.class, () -> service.modifyUser(1, newUser));
	}
}
