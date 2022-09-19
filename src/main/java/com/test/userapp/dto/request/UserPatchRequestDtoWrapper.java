package com.test.userapp.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchRequestDtoWrapper {
    @Valid
    private UserPatchRequestDto data;
}
