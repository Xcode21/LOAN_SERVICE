package com.xcode.loanservice.r2dbc.mapper;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class SqlLoader {
    public String load(String path) {
        try {
            return new String(
                    Objects.requireNonNull(
                            getClass().getClassLoader().getResourceAsStream(path)
                    ).readAllBytes(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Error loading SQL file: " + path, e);
        }
    }
}
