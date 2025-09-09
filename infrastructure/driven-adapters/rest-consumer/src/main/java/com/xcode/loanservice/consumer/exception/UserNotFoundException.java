package com.xcode.loanservice.consumer.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String documento) {
        super("Usuario con documento " + documento + " no encontrado");
    }
}

