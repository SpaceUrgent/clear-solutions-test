package com.test.userapp.dto.request;

import com.test.userapp.lib.ValidBirthDate;
import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {
    @Email
    private String email;
    @NotNull
    @Size(min = 1, max = 30)
    @Pattern(regexp = "[A-Z][a-z]*")
    private String firstName;
    @NotNull
    @Size(min = 1, max = 30)
    @Pattern(regexp = "[A-Z][a-z]*")
    private String lastName;
    @NotNull
    @ValidBirthDate
    private LocalDate birthDate;
    private String address;
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
        message = "Invalid phone number format")
    private String phone;
}
