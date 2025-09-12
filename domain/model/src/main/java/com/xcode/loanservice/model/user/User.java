package com.xcode.loanservice.model.user;

import lombok.*;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private UUID idUser;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private String document;
    private Double salaryBase;
    private Role role;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static User createWebClient(String email,Double salaryBase){
        return User.builder()
                .email(email.trim().toLowerCase())
                .salaryBase(salaryBase)
                .build();
    }
}