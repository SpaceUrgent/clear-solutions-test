package com.test.userapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.userapp.dto.request.UserCreateRequestDto;
import com.test.userapp.dto.request.UserCreateRequestDtoWrapper;
import com.test.userapp.entity.User;
import com.test.userapp.service.UserService;
import com.test.userapp.service.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserController_Test {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    @Test
    public void register_returnsCreatedWithLocationHeader() throws Exception {
        Mockito.when(this.userMapper.fromDto(any(UserCreateRequestDto.class))).thenReturn(getUser());
        Mockito.when(userService.save(any(User.class))).thenReturn(getUserWithId());

        MvcResult mvcResult = this.mvc.perform(post("/users/register")
                        .content(asJsonString(getCreateRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        String header = mvcResult.getResponse().getHeader("Location");
        assertEquals("/users/1", header);
    }

    @Test
    public void register_userWithInvalidInputs_ReturnsBadRequestAndJson() throws Exception {
        Mockito.when(this.userMapper.fromDto(any(UserCreateRequestDto.class))).thenReturn(getUser());
        Mockito.when(userService.save(any(User.class))).thenReturn(getUserWithId());


        for (int i = 0; i < 5; i++) {
            this.mvc.perform(post("/users/register")
                            .content(asJsonString(getInvalidRequests().get(i)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.description", is("400 BAD_REQUEST")))
                    .andExpect(jsonPath("$.details", is(List.of(getInvalidInputMessage().get(i)))));
        }
    }

    private User getUser() {
        User user = new User();
        user.setFirstName("Bob");
        user.setFirstName("Surname");
        user.setBirthDate(LocalDate.of(2000, 11, 10));
        user.setAddress("some address");
        user.setPhone("+380631231212");
        return user;
    }

    private User getUserWithId() {
        User user = getUser();
        user.setId(1L);
        return user;
    }

    private UserCreateRequestDtoWrapper getCreateRequest() {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(
                "user@domain.com",
                "Bob",
                "Surname",
                LocalDate.of(2000, 11, 10),
                "some address",
                "+380631231212"
        );
        return new UserCreateRequestDtoWrapper(userCreateRequestDto);
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<UserCreateRequestDtoWrapper> getInvalidRequests() {
        List<UserCreateRequestDto> userCreateRequestDtos = List.of(
                new UserCreateRequestDto("email", "Bob", "Bob",
                        LocalDate.of(2000, 10, 10), "", ""),
                new UserCreateRequestDto("email@domain.com", "", "Bob",
                        LocalDate.of(2000, 10, 10), "", ""),
                new UserCreateRequestDto("email@domain.com", "Bob", "",
                        LocalDate.of(2000, 10, 10), "", ""),
                new UserCreateRequestDto("email@domain.com", "Bob", "Bob",
                        LocalDate.of(2010, 10, 10), "", ""),
                new UserCreateRequestDto("email@domain.com", "Bob", "Bob",
                        null, "", "")
        );
        return userCreateRequestDtos.stream()
                .map(UserCreateRequestDtoWrapper::new)
                .collect(Collectors.toList());
    }

    private List<String> getInvalidInputMessage() {
        return List.of(
                "email must be a well-formed email address",
                "firstName size must be between 1 and 30",
                "lastName size must be between 1 and 30",
                "birthDate Invalid birth date. The user has to be older than 18 years old",
                "birthDate must not be null"
        );
    }
}
