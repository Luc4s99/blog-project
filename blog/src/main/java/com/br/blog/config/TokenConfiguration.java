package com.br.blog.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.br.blog.entity.RefreshToken;
import com.br.blog.entity.User;
import com.br.blog.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenConfiguration {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token.duration}")
    private long tokenDuration;

    @Value("${jwt.refresh-token.duration}")
    private long refreshTokenDuration;

    private final RefreshTokenService refreshTokenService;

    public TokenConfiguration(RefreshTokenService refreshTokenService) {

        this.refreshTokenService = refreshTokenService;
    }

    public String generateToken(User user) {

        String token = JWT.create()
                .withClaim("userId", user.getId())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(this.tokenDuration))
                .withIssuedAt(Instant.now())
                .sign(Algorithm.HMAC256(secret));

        return token;
    }

    public RefreshToken generateRefreshToken(User user) {

        // Revogar tokens antigos do usuário
        revokeUserToken(user.getId());

        String refreshToken = JWT.create()
                .withClaim("userId", user.getId())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(this.refreshTokenDuration))
                .withIssuedAt(Instant.now())
                .sign(Algorithm.HMAC256(secret));

        RefreshToken refreshTokenObj = new RefreshToken(false,
                                        Date.from(Instant.now().plusSeconds(this.refreshTokenDuration)),
                                        user.getId(),
                                        refreshToken);

        refreshTokenService.saveRefreshToken(refreshTokenObj);
        return refreshTokenObj;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiresAt().before(new Date())) {

            token.setRevoked(true);
            refreshTokenService.saveRefreshToken(token);
        }

        return token;
    }

    private void revokeUserToken(String userId) {

        RefreshToken userToken = refreshTokenService.getRefreshTokenByUser(userId);

        if(userToken != null) {

            refreshTokenService.deleteRefreshToken(userToken);
        }
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
