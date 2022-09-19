package com.test.userapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class UserResponseDtoWrapper {
    private List<UserResponseDto> data;
}
