package com.test.userapp.service.mapper;

import com.test.userapp.dto.request.UserCreateRequestDto;
import com.test.userapp.dto.request.UserPatchRequestDto;
import com.test.userapp.dto.response.UserResponseDto;
import com.test.userapp.entity.User;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserMapper_Test {
    private UserMapper userMapper;

    @BeforeAll
    public void setup() {
        userMapper = new UserMapperImpl();
    }

    @AfterAll
    public void close() {}

    @Test
    public void fromDto_ok() {
        User expected1 = new User(null, "email@domain.com", "Bob", "Surname",
                LocalDate.of(2000, 10, 10), "", "");
        User expected2 = new User();
        expected2.setEmail("email@domain.com");
        UserCreateRequestDto userCreateRequestDto =
                new UserCreateRequestDto("email@domain.com", "Bob", "Surname",
                        LocalDate.of(2000, 10, 10), "", "");
        UserPatchRequestDto userPatchRequestDto = new UserPatchRequestDto();
        userPatchRequestDto.setEmail("email@domain.com");
        User actual1 = userMapper.fromDto(userCreateRequestDto);
        User actual2 = userMapper.fromDto(userPatchRequestDto);
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toDto_ok() {
        User user = new User(1L, "email@domain.com", "Bob", "Surname",
                LocalDate.of(2000, 10, 10), "", "");
        UserResponseDto expected = new UserResponseDto(1L, "email@domain.com", "Bob", "Surname",
                LocalDate.of(2000, 10, 10), "", "");
        UserResponseDto actual = userMapper.toDto(user);
        assertEquals(expected, actual);
    }
}
