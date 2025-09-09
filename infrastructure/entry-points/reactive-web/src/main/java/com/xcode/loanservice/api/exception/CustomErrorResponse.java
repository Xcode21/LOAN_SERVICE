package com.xcode.loanservice.api.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorResponse {
    private String code;
    private String message;
    private Object data;
    private String path;
    
    @JsonIgnore
    private Integer statusCode;
    @JsonIgnore
    private String logLevel;

}