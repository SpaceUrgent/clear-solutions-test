//package com.test.userapp.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.test.userapp.dto.request.UserCreateRequestDto;
//import com.test.userapp.dto.request.UserCreateRequestDtoWrapper;
//import com.test.userapp.entity.User;
//import com.test.userapp.service.UserService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDate;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private UserService userService;
//
//    @BeforeEach
//    void setup() {
//        Rest
//    }
//
//    @Test
//    public void register_returnsCreatedWithLocationHeader() throws Exception {
//        Mockito.when(userService.save(ArgumentMatchers.any())).thenReturn(getUser());
//        UserCreateRequestDtoWrapper userCreateRequestDtoWrapper = getCreateRequest();
//        String jsonContent = asJsonString(userCreateRequestDtoWrapper);
//        MvcResult mvcResult = mvc.perform(post("/users/register")
//                        .content(jsonContent)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andReturn();
//        String header = mvcResult.getResponse().getHeader("Location");
//        Assertions.assertEquals("/users/1", header);
//    }
//
//    private User getUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setFirstName("Bob");
//        user.setFirstName("Surname");
//        user.setBirthDate(LocalDate.of(2000, 11, 10));
//        user.setAddress("some address");
//        user.setPhone("+380631231212");
//        return user;
//    }
//
//    private UserCreateRequestDtoWrapper getCreateRequest() {
//        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(
//                "user@domain.com",
//                "Bob",
//                "Surname",
//                LocalDate.of(2000, 11, 10),
//                "some address",
//                "+380631231212"
//        );
//        return new UserCreateRequestDtoWrapper(userCreateRequestDto);
//    }
//
//    private String asJsonString(final Object obj) {
//        try {
//            final ObjectMapper mapper = new ObjectMapper();
//            mapper.registerModule(new JavaTimeModule());
//            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//            final String jsonContent = mapper.writeValueAsString(obj);
//            return jsonContent;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
