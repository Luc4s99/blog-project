package com.br.blog.service;

import com.br.blog.entity.RefreshToken;
import com.br.blog.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {

        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken saveRefreshToken(RefreshToken refreshToken) {

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getRefreshTokenByToken(String refreshToken) {

        return refreshTokenRepository.findByToken(refreshToken);
    }

    public RefreshToken getRefreshTokenByUser(String user) {

        return refreshTokenRepository.findByUser(user);
    }

    public void deleteRefreshToken(RefreshToken refreshToken) {

        refreshTokenRepository.delete(refreshToken);
    }
}
