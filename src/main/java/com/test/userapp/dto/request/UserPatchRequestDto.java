package com.test.userapp.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.userapp.lib.ValidBirthDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
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
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Invalid phone number format")
    private String phone;
}
