package com.br.blog.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.br.blog.dto.response.LoginResponse;
import com.br.blog.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfiguration {

    @Value("${jwt.secret}")
    private String secret;

    private final long tokenDuration = 3600;

    public String generateToken(User user) {

        String token = JWT.create()
                .withClaim("userId", user.getId())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(this.tokenDuration))
                .withIssuedAt(Instant.now())
                .sign(Algorithm.HMAC256(secret));

        return token;
    }

    public Optional<JWTUserData> validateToken(String token) {

        try {

            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);

            JWTUserData jwtUserData = new JWTUserData(decodedJWT.getClaim("userId").asString(), decodedJWT.getSubject());

            return Optional.of(jwtUserData);
        } catch(JWTVerificationException exception) {

            return Optional.empty();
        }
    }
}
