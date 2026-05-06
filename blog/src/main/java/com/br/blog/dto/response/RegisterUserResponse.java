package com.br.blog.dto.response;

import com.br.blog.entity.User;

public record RegisterUserResponse(User user, String token) {
}
