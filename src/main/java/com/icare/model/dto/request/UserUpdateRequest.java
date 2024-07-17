package com.icare.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zƏəÖöÜüŞşÇçığ]+$", message = "Name must contain only Azerbaijani letters")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zƏəÖöÜüŞşÇçığ]+$", message = "Surname must contain only Azerbaijani letters")
    @NotBlank(message = "Surname cannot be blank")
    private String surname;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "^\\+994\\d{9}$", message = "Phone number must be in the format +994XXXXXXXXX")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;

    private String photoUrl;
}
