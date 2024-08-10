package com.icare.model.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ValidationExceptionResponse {
    private String timestamp;
    private String code;
    private Integer status;
    private List<FieldErrorResponse> fieldErrors;
}
