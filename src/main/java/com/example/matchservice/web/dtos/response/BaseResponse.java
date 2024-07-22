package com.example.matchservice.web.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder @Getter @Setter
public class BaseResponse {

    private Object data;

    private String message;

    private Boolean success;

    private Integer statusCode;

    private HttpStatus httpStatus;
}
