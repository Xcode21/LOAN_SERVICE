package com.xcode.loanservice.api.filter;


import com.xcode.loanservice.api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return jwtUtil.extractAllClaims(token)
                    .map(claims -> {
                        String documento = claims.getSubject();
                        String role = claims.get("role", String.class);
                        String email = claims.get("email", String.class);
                        List<GrantedAuthority> authorities = List.of(
                                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                        );
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(documento, token, authorities);

                        auth.setDetails(email);
                        return auth;
                    })
                    .flatMap(auth ->
                            chain.filter(exchange)
                                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
                    );
        }
        return chain.filter(exchange);
    }
}
