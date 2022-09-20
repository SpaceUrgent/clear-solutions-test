package com.test.userapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.userapp.dto.request.UserCreateRequestDto;
import com.test.userapp.dto.request.UserCreateRequestDtoWrapper;
import com.test.userapp.dto.request.UserPatchRequestDto;
import com.test.userapp.dto.request.UserPatchRequestDtoWrapper;
import com.test.userapp.entity.User;
import com.test.userapp.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = WebEnvironment.MOCK,
        classes = UserAppApplication.class)
@AutoConfigureMockMvc
public class UserAppIntegration_Test {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository repository;

    @Test
    @Order(1)
    public void register_ok() throws Exception {
        UserCreateRequestDtoWrapper createRequest = getCreateRequest();

        MvcResult mvcResult = mvc.perform(post("/users/register")
                        .content(asJsonString(createRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].email", is("emai@domain.com")))
                .andExpect(jsonPath("$.data[0].firstName", is("Bob")))
                .andExpect(jsonPath("$.data[0].lastName", is("Johnson")))
                .andExpect(jsonPath("$.data[0].birthDate", is("2000-10-10")))
                .andExpect(jsonPath("$.data[0].address", is("some address")))
                .andExpect(jsonPath("$.data[0].phone", is("+380631231212")))
                .andReturn();
        String header = mvcResult.getResponse().getHeader("Location");
        Assertions.assertEquals("/users/1", header);

        Optional<User> user = repository.findById(1L);
        assertTrue(user.isPresent());
        assertEquals(1L, user.get().getId());
        assertEquals("emai@domain.com", user.get().getEmail());
        assertEquals("Bob", user.get().getFirstName());
        assertEquals("Johnson", user.get().getLastName());
        assertEquals(LocalDate.of(2000, 10, 10), user.get().getBirthDate());
        assertEquals("some address", user.get().getAddress());
        assertEquals("+380631231212", user.get().getPhone());
    }

    @Test
    @Order(2)
    public void patch_ok() throws Exception {
        UserPatchRequestDto userPatchRequestDto = new UserPatchRequestDto();
        userPatchRequestDto.setEmail("bob@domain.com");
        UserPatchRequestDtoWrapper patchRequest = new UserPatchRequestDtoWrapper(userPatchRequestDto);

        mvc.perform(patch("/users/1")
                        .content(asJsonString(patchRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].email", is("bob@domain.com")))
                .andExpect(jsonPath("$.data[0].firstName", is("Bob")))
                .andExpect(jsonPath("$.data[0].lastName", is("Johnson")))
                .andExpect(jsonPath("$.data[0].birthDate", is("2000-10-10")))
                .andExpect(jsonPath("$.data[0].address", is("some address")))
                .andExpect(jsonPath("$.data[0].phone", is("+380631231212")));

        Optional<User> user = repository.findById(1L);
        assertTrue(user.isPresent());
        assertEquals(1L, user.get().getId());
        assertEquals("bob@domain.com", user.get().getEmail());
        assertEquals("Bob", user.get().getFirstName());
        assertEquals("Johnson", user.get().getLastName());
        assertEquals(LocalDate.of(2000, 10, 10), user.get().getBirthDate());
        assertEquals("some address", user.get().getAddress());
        assertEquals("+380631231212", user.get().getPhone());
    }

    @Test
    @Order(3)
    public void patch_withNonExistingId_returnBadRequestAndJson() throws Exception {
        UserPatchRequestDto userPatchRequestDto = new UserPatchRequestDto();
        userPatchRequestDto.setEmail("user@domain.com");
        UserPatchRequestDtoWrapper patchRequest = new UserPatchRequestDtoWrapper(userPatchRequestDto);

        mvc.perform(patch("/users/100")
                        .content(asJsonString(patchRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.description", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.details", is("Can't find user with id 100")));
    }

    private UserCreateRequestDtoWrapper getCreateRequest() {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(
                "emai@domain.com", "Bob", "Johnson",
                LocalDate.of(2000, 10, 10), "some address", "+380631231212"
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
