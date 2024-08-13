package com.icare.dto.request;

import com.icare.util.annotation.ValidEmail;
import com.icare.util.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}