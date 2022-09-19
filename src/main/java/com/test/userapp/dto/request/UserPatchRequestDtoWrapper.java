package com.test.userapp.dto.request;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class UserPatchRequestDtoWrapper {
    @Valid
    private UserPatchRequestDto data;
}
