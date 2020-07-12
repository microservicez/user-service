package com.github.userservice.units.controller;

import static com.github.userservice.constants.UserConstants.BASE_URL;
import static com.github.userservice.constants.UserConstants.ERROR_MESSAGE;
import static com.github.userservice.constants.UserConstants.USER_ID;
import static com.github.userservice.constants.UserConstants.USER_WITH_USER_ID_1_NOT_FOUND;
import static com.github.userservice.constants.UserConstants.UTF8;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.userservice.controller.UserController;
import com.github.userservice.dto.User;
import com.github.userservice.exception.UserNotFoundException;
import com.github.userservice.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    public void addUser_shouldAddUser() throws Exception {
        User user = new User();
        user.setName("John Doe");

        User persistedUser = new User(1, user.getName());

        given(service.addUser(user)).willReturn(persistedUser);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(APPLICATION_JSON).characterEncoding(UTF8)
                .content(mapper.writeValueAsString(user)).accept(APPLICATION_JSON))

                .andExpect(status().isCreated()).andExpect(jsonPath("id").value(persistedUser.getId()))
                .andExpect(jsonPath("name").value(persistedUser.getName()));
    }

    @Test
    public void getUser_ShouldReturnUser() throws Exception {
        given(service.getUser(1)).willReturn(new User(1, "John"));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + USER_ID)).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1)).andExpect(jsonPath("name").value("John"));
    }

    @Test
    public void getUser_shouldThowUserNotFoundException() throws Exception {
        given(service.getUser(1)).willThrow(new UserNotFoundException(1));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + USER_ID)).andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_MESSAGE).value(USER_WITH_USER_ID_1_NOT_FOUND));
    }

    @Test
    public void getUsers_shouldReturnUserList() throws Exception {
        List<User> users = List.of(new User(1, "John Doe"), new User(2, "Jane Doe"));
        given(service.getAllUsers()).willReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].name", is("Jane Doe")));
    }

    @Test
    public void getUsers_shouldReturnEmptyResult() throws Exception {
        given(service.getAllUsers()).willReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void removeUser_shouldRemoveUser() throws Exception {
        doAnswer(x -> null).when(service).removeUser(1);
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + USER_ID)).andExpect(status().isNoContent());
    }

    @Test
    public void removeUser_shouldThrowUserNotFoundException() throws Exception {
        doThrow(new UserNotFoundException(1)).when(service).removeUser(1);

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + USER_ID)).andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_MESSAGE).value(USER_WITH_USER_ID_1_NOT_FOUND));
    }

    @Test
    public void modifyUser_shouldEditTheUser() throws Exception {
        User newUser = new User(1, "Jane Doe");
        given(service.modifyUser(1, newUser)).willReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + USER_ID).contentType(APPLICATION_JSON)
                .characterEncoding(UTF8).content(mapper.writeValueAsString(newUser)).accept(APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("id").value(newUser.getId()))
                .andExpect(jsonPath("name").value(newUser.getName()));
    }

    @Test
    public void modifyUser_shouldThrowUserNotFoundException() throws Exception {
        User newUser = new User(1, "Jane Doe");
        given(service.modifyUser(1, newUser)).willThrow(new UserNotFoundException(1));

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + USER_ID).contentType(APPLICATION_JSON)
                .characterEncoding(UTF8).content(mapper.writeValueAsString(newUser)).accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_MESSAGE).value(USER_WITH_USER_ID_1_NOT_FOUND));
    }
}
