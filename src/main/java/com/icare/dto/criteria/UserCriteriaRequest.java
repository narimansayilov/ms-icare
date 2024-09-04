package com.icare.dto.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCriteriaRequest {
    private String username;
    private Boolean status;
}
