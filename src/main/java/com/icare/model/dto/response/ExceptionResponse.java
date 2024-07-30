package com.icare.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ExceptionResponse {
    private String timestamp;
    private String message;
    private String code;
    private Integer status;
    private List<FieldErrorResponse> fieldErrors;
}
