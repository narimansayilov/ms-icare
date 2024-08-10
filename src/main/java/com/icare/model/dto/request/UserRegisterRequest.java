package com.icare.model.dto.request;

import com.icare.util.annotation.ValidEmail;
import com.icare.util.annotation.ValidPassword;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRegisterRequest {
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

    @Size(min = 7, max = 7, message = "Pin must be exactly 7 characters long")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Pin must contain only English letters and numbers")
    @NotBlank(message = "Pin cannot be blank")
    private String pin;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zƏəÖöÜüŞşÇçığ]+$", message = "Name must contain only Azerbaijani letters")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zƏəÖöÜüŞşÇçığ]+$", message = "Surname must contain only Azerbaijani letters")
    @NotBlank(message = "Surname cannot be blank")
    private String surname;

    @Past(message = "Birth day must be a past date")
    @NotNull(message = "Birth day cannot be blank")
    private LocalDate birthDay;

    @Pattern(regexp = "^\\+994\\d{9}$", message = "Phone number must be in the format +994XXXXXXXXX")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
}
