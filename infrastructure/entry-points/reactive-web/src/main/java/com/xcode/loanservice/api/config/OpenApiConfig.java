package com.xcode.loanservice.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8098}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Loan Service API")
                .description("API REST para la gestión de solicitudes de préstamos. " +
                    "Este servicio permite crear, validar y gestionar solicitudes de préstamos de diferentes tipos.")
                .version("v1.0.0")
                .contact(new Contact()
                    .name("Equipo de Desarrollo")
                    .email("desarrollo@pragma.com.co")
                    .url("https://pragma.com.co"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Servidor de desarrollo local"),
                new Server()
                    .url("https://api.pragma.com.co")
                    .description("Servidor de producción")
            ));
    }
}