package com.test.userapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.userapp.dto.request.UserCreateRequestDto;
import com.test.userapp.dto.request.UserCreateRequestDtoWrapper;
import com.test.userapp.dto.request.UserPatchRequestDto;
import com.test.userapp.dto.request.UserPatchRequestDtoWrapper;
import com.test.userapp.dto.response.UserResponseDto;
import com.test.userapp.entity.User;
import com.test.userapp.service.UserService;
import com.test.userapp.service.mapper.UserMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserController_Test {
    private List<UserCreateRequestDtoWrapper> invalidCreateRequests;
    private List<UserPatchRequestDtoWrapper> invalidPatchRequests;
    private List<String> errorMessages;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    @Before
    public void setUp() throws Exception {
        List<UserCreateRequestDto> userCreateRequestDtos = List.of(
                new UserCreateRequestDto("email", "Bob", "Bob",
                        LocalDate.of(2000, 10, 10), "", null),
                new UserCreateRequestDto("email@domain.com", "", "Bob",
                        LocalDate.of(2000, 10, 10), "", null),
                new UserCreateRequestDto("email@domain.com", "Bob", "",
                        LocalDate.of(2000, 10, 10), "", null),
                new UserCreateRequestDto("email@domain.com", "Bob", "Bob",
                        LocalDate.of(2010, 10, 10), "", null),
                new UserCreateRequestDto("email@domain.com", "Bob", "Bob",
                        null, "", null)
        );
        invalidCreateRequests = userCreateRequestDtos.stream()
                .map(UserCreateRequestDtoWrapper::new)
                .collect(Collectors.toList());


        errorMessages = List.of(
                "email must be a well-formed email address",
                "firstName size must be between 1 and 30",
                "lastName size must be between 1 and 30",
                "birthDate Invalid birth date. The user has to be older than 18 years old",
                "birthDate must not be null"
        );

        List<UserPatchRequestDto> userPatchRequestDtos = List.of(
                new UserPatchRequestDto("email", null, null,
                        null, null, null),
                new UserPatchRequestDto(null, "", null,
                        null, null, null),
                new UserPatchRequestDto(null, null, "",
                        null, null, null),
                new UserPatchRequestDto(null, null, null,
                        LocalDate.of(2020, 01, 01), null, null)
        );
        invalidPatchRequests = userPatchRequestDtos.stream()
                .map(UserPatchRequestDtoWrapper::new)
                .collect(Collectors.toList());
    }

    @Test
    public void register_returnsCreatedWithLocationHeaderAndJson() throws Exception {
        Mockito.when(this.userMapper.fromDto(any(UserCreateRequestDto.class))).thenReturn(getUser());
        Mockito.when(userService.save(any(User.class))).thenReturn(getUserWithId());
        Mockito.when(this.userMapper.toDto(any())).thenReturn(getUserResponse());

        MvcResult mvcResult = this.mvc.perform(post("/users/register")
                        .content(asJsonString(getCreateRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].email", is("email@domain.com")))
                .andExpect(jsonPath("$.data[0].firstName", is("Bob")))
                .andExpect(jsonPath("$.data[0].lastName", is("Surname")))
                .andExpect(jsonPath("$.data[0].birthDate", is("2000-11-10")))
                .andExpect(jsonPath("$.data[0].address", is("some address")))
                .andExpect(jsonPath("$.data[0].phone", is("+380631231212")))
                .andReturn();
        String header = mvcResult.getResponse().getHeader("Location");
        assertEquals("/users/1", header);
    }

    @Test
    public void register_userWithInvalidInputs_returnsBadRequestAndJson() throws Exception {
        Mockito.when(this.userMapper.fromDto(any(UserCreateRequestDto.class))).thenReturn(getUser());
        Mockito.when(userService.save(any(User.class))).thenReturn(getUserWithId());


        for (int i = 0; i < 5; i++) {
            this.mvc.perform(post("/users/register")
                            .content(asJsonString(invalidCreateRequests.get(i)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.description", is("400 BAD_REQUEST")))
                    .andExpect(jsonPath("$.details[0]", is(errorMessages.get(i))));
        }
    }

    @Test
    public void patch_returnsOk()
            throws Exception {
        Mockito.when(this.userMapper.fromDto(any(UserCreateRequestDto.class))).thenReturn(getUser());
        Mockito.when(userService.update(anyLong(), any(User.class))).thenReturn(getUserWithId());
        Mockito.when(this.userMapper.toDto(any())).thenReturn(getUserResponse());

        UserPatchRequestDto userPatchRequestDto = new UserPatchRequestDto();
        userPatchRequestDto.setEmail("email@domain.con");
        UserPatchRequestDtoWrapper userPatchRequestDtoWrapper =
                new UserPatchRequestDtoWrapper(userPatchRequestDto);
        this.mvc.perform(patch("/users/1")
                .content(asJsonString(userPatchRequestDtoWrapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].email", is("email@domain.com")))
                .andExpect(jsonPath("$.data[0].firstName", is("Bob")))
                .andExpect(jsonPath("$.data[0].lastName", is("Surname")))
                .andExpect(jsonPath("$.data[0].birthDate", is("2000-11-10")))
                .andExpect(jsonPath("$.data[0].address", is("some address")))
                .andExpect(jsonPath("$.data[0].phone", is("+380631231212")));
    }

    @Test
    public void patch_withInvalidInputs_returnsBadRequestAndJson()
            throws Exception {
        Mockito.when(this.userMapper.fromDto(any(UserCreateRequestDto.class))).thenReturn(getUser());
        Mockito.when(userService.update(anyLong(), any(User.class))).thenReturn(getUserWithId());

        for (int i = 0; i < invalidPatchRequests.size(); i++) {
            this.mvc.perform(patch("/users/" + i)
                    .content(asJsonString(invalidPatchRequests.get(i)))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.description", is("400 BAD_REQUEST")))
                    .andExpect(jsonPath("$.details", is(List.of(errorMessages.get(i)))));
        }
    }

    @Test
    public void update_returnsOk() throws Exception {
        Mockito.when(this.userMapper.fromDto(any(UserCreateRequestDto.class))).thenReturn(getUser());
        Mockito.when(userService.update(anyLong(), any(User.class))).thenReturn(getUserWithId());
        Mockito.when(this.userMapper.toDto(any())).thenReturn(getUserResponse());

        UserCreateRequestDtoWrapper updateRequest = getCreateRequest();
        this.mvc.perform(put("/users/1")
                        .content(asJsonString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].email", is("email@domain.com")))
                .andExpect(jsonPath("$.data[0].firstName", is("Bob")))
                .andExpect(jsonPath("$.data[0].lastName", is("Surname")))
                .andExpect(jsonPath("$.data[0].birthDate", is("2000-11-10")))
                .andExpect(jsonPath("$.data[0].address", is("some address")))
                .andExpect(jsonPath("$.data[0].phone", is("+380631231212")));
    }

    @Test
    public void delete_returnsOk() throws Exception {
        Mockito.when(userService.delete(anyLong())).thenReturn(true);

        this.mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void findWithinBirthDateRange_returnsOkAndJson() throws Exception {
        Mockito.when(userService.searchByBirthDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(new User()));
        UserResponseDto userResponseDto =
                new UserResponseDto(10L, "email@domain.com", "Bilbo", "Baggings",
                        LocalDate.of(1980, 10, 10), "", "");
        Mockito.when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        mvc.perform(get("/users?from=1980-01-01&to=2000-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(10)))
                .andExpect(jsonPath("$.data[0].email", is("email@domain.com")))
                .andExpect(jsonPath("$.data[0].firstName", is("Bilbo")))
                .andExpect(jsonPath("$.data[0].lastName", is("Baggings")))
                .andExpect(jsonPath("$.data[0].birthDate", is("1980-10-10")))
                .andExpect(jsonPath("$.data[0].address", is("")))
                .andExpect(jsonPath("$.data[0].phone", is("")));
    }

    @Test
    public void findWithinBirthDateRange_withInvalidParams_returnsBadRequestAndJson()
            throws Exception {
        mvc.perform(get("/users?from=2020-01-01&to=2018-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("description", is(HttpStatus.BAD_REQUEST.toString())))
                .andExpect(jsonPath("details",
                        is("Wrong search date range: 2020-01-01 can't be after 2018-01-01")));
    }
    private User getUser() {
        User user = new User();
        user.setEmail("email@domain.com");
        user.setFirstName("Bob");
        user.setLastName("Surname");
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

    private UserResponseDto getUserResponse() {
        UserResponseDto userResponseDto = new UserResponseDto(
                1L,
                "email@domain.com",
                "Bob",
                "Surname",
            LocalDate.of(2000, 11, 10),
            "some address",
            "+380631231212");
        return userResponseDto;
    }

    private UserCreateRequestDtoWrapper getCreateRequest() {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(
                "email@domain.com",
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
}
