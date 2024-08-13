package com.icare.dto.request;

import com.icare.util.annotation.ValidEmail;
import com.icare.util.annotation.ValidPhoneNumber;
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