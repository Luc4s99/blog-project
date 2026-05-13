package com.br.blog.dto.request;

import com.br.blog.entity.User;

public record RefreshTokenRequest(User user) {
}
