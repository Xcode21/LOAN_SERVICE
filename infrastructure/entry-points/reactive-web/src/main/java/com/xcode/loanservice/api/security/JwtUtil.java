package com.xcode.loanservice.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private SecretKey signingKey;

    @PostConstruct
    private void init() {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public Mono<String> extractDocumentFromToken(String token) {
        return extractClaim(token, Claims::getSubject)
                .doOnNext(doc -> log.debug("Documento extraído del token: {}", doc))
                .doOnError(error -> log.warn("Error extrayendo documento del token: {}", error.getMessage()));
    }


    public Mono<Boolean> validateToken(String token) {
        return parseClaims(token)
                .map(claims -> {
                    boolean valid = !claims.getExpiration().before(new Date());
                    log.debug("Token válido: {}", valid);
                    return valid;
                })
                .onErrorResume(ExpiredJwtException.class, e -> {
                    log.warn("Token expirado: {}", e.getMessage());
                    return Mono.just(false);
                })
                .onErrorResume(JwtException.class, e -> {
                    log.warn("Token inválido: {}", e.getMessage());
                    return Mono.just(false);
                });
    }

    public Mono<Claims> extractAllClaims(String token) {
        return parseClaims(token)
                .doOnNext(claims -> log.debug("Claims extraídos: {}", claims))
                .doOnError(error -> log.warn("Error extrayendo claims: {}", error.getMessage()));
    }

    public <T> Mono<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
        return parseClaims(token).map(claimsResolver);
    }

    private Mono<Claims> parseClaims(String token) {
        return Mono.fromCallable(() -> parseToken(token))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}