package com.xcode.loanservice.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ApplicationRouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(ApplicationHandler applicationHandler) {
        return route(POST("/api/v1/application"), applicationHandler::createApplication);
    }
}
