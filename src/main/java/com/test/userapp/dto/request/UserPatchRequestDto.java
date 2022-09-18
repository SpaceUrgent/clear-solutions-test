package com.test.userapp.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.userapp.lib.ValidBirthDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties
public class UserPatchRequestDto {
    @Email
    private String email;
    @Size(min = 1, max = 30)
    private String firstName;
    @Size(min = 1, max = 30)
    private String lastName;
    @ValidBirthDate
    private LocalDate birthDate;
    private String address;
    private String phone;
}
