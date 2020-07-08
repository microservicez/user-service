package com.github.userservice.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.userservice.dto.User;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceIT {
	private static final String USER_ID = "1";
	private static final String BASE_URL = "/api/v1/users/";

	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	@Order(1)
	public void createUser() throws Exception {
		User user = new User();
		user.setName("John Doe");
		ResponseEntity<User> response = restTemplate.postForEntity(BASE_URL, user, User.class);
		User persistedUser = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(persistedUser.getId()).isEqualTo(1);
		assertThat(persistedUser.getName()).isEqualTo(user.getName());
	}
	
	@Test
	@Order(2)
	public void getAllUsers() throws Exception {
		ResponseEntity<Object> response = restTemplate.getForEntity(BASE_URL, Object.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	@Order(3)
	public void getUser() throws Exception {
		ResponseEntity<User> response = restTemplate.getForEntity(BASE_URL+USER_ID, User.class);
		User user = response.getBody();
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(user.getId()).isEqualTo(1);
		assertThat(user.getName()).isEqualTo("John Doe");
	}
	
	@Test
	@Order(4)
	public void modifyUser() throws Exception {
		User user = new User();
		user.setName("Jane Doe");
		ResponseEntity<User> response = restTemplate.exchange(BASE_URL+USER_ID, HttpMethod.PUT, new HttpEntity<User>(user), User.class);
		User persistedUser = response.getBody();
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(persistedUser.getId()).isEqualTo(1);
		assertThat(persistedUser.getName()).isEqualTo(user.getName());
	}
	
	@Test
	@Order(5)
	public void removeUser() throws Exception {
		ResponseEntity<Object> response = restTemplate.exchange(BASE_URL+USER_ID, HttpMethod.DELETE, null, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	public void removeUser_throwsUserNotFoundExcpetion() throws Exception {
		ResponseEntity<String> response = restTemplate.exchange(BASE_URL+USER_ID, HttpMethod.DELETE, null, String.class);
		assertUserNotFoundScenario(response);
	}
	
	@Test
	public void getUser_throwsUserNotFoundExcpetion() throws Exception {
		ResponseEntity<String> response = restTemplate.exchange(BASE_URL+USER_ID, HttpMethod.GET, null, String.class);
		assertUserNotFoundScenario(response);
	}
	
	@Test
	public void modifyUser_throwsUserNotFoundExcpetion() throws Exception {
		User user = new User();
		ResponseEntity<String> response = restTemplate.exchange(BASE_URL+USER_ID, HttpMethod.PUT, new HttpEntity<User>(user), String.class);
		assertUserNotFoundScenario(response);
	}
	
	private void assertUserNotFoundScenario(ResponseEntity<String> response) throws Exception {
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		JSONObject json = new JSONObject(response.getBody());
		
		assertThat(json.get("errorMessage")).isEqualTo("user with user id: 1 not found");
	}
}
