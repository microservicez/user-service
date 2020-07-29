package com.github.userservice.integration;

import static com.github.userservice.constants.UserConstants.BASE_URL;
import static com.github.userservice.constants.UserConstants.USER_ID;
import static com.github.userservice.constants.UserConstants.USER_WITH_USER_ID_1_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.userservice.dto.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceIT {

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
        ResponseEntity<User> response = restTemplate.getForEntity(BASE_URL + USER_ID, User.class);
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
        ResponseEntity<User> response = restTemplate.exchange(BASE_URL + USER_ID, PUT, new HttpEntity<User>(user),
                User.class);
        User persistedUser = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(persistedUser.getId()).isEqualTo(1);
        assertThat(persistedUser.getName()).isEqualTo(user.getName());
    }

    @Test
    @Order(5)
    public void removeUser() throws Exception {
        ResponseEntity<Object> response = restTemplate.exchange(BASE_URL + USER_ID, DELETE, null, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void removeUser_throwsUserNotFoundExcpetion() throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + USER_ID, DELETE, null, String.class);
        assertUserNotFoundScenario(response);
    }

    @Test
    public void getUser_throwsUserNotFoundExcpetion() throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + USER_ID, GET, null, String.class);
        assertUserNotFoundScenario(response);
    }

    @Test
    public void modifyUser_throwsUserNotFoundExcpetion() throws Exception {
        User user = new User();
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + USER_ID, PUT, new HttpEntity<User>(user),
                String.class);
        assertUserNotFoundScenario(response);
    }

    private void assertUserNotFoundScenario(ResponseEntity<String> response) throws Exception {
        JSONObject json = new JSONObject(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(json.get("errorMessage")).isEqualTo(USER_WITH_USER_ID_1_NOT_FOUND);
    }
}
