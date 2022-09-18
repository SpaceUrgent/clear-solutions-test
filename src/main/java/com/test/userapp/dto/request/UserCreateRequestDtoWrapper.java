package com.test.userapp.dto.request;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class UserCreateRequestDtoWrapper {
    @Valid
    private UserCreateRequestDto data;
}
