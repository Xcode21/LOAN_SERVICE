package com.xcode.loanservice.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationResponse {
    private String documento;
    private boolean active;
    private String status;
    private String message;
    private LocalDateTime validatedAt;

    public UserValidationResponse(boolean active) {
        this.active = active;
    }
}
