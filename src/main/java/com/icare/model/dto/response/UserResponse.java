package com.icare.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponse {
    private int id;
    private String name;
    private String surname;
    private LocalDate birthDay;
    private String email;
    private String phoneNumber;
    private String photoUrl;
}
