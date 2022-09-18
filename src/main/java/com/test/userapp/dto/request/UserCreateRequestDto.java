package com.test.userapp.dto.request;

import com.test.userapp.lib.ValidBirthDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateRequestDto {
    @Email
    private String email;
    @NotNull
    @Size(min = 1, max = 30)
    private String firstName;
    @NotNull
    @Size(min = 1, max = 30)
    private String lastName;
    @NotNull
    @ValidBirthDate
    private LocalDate birthDate;
    private String address;
    private String phone;
}
