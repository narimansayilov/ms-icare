package com.icare.model.dto.request;

import com.icare.util.annotation.ValidEmail;
import com.icare.util.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @ValidEmail
    private String email;

    @ValidPhoneNumber
    private String phoneNumber;
}
