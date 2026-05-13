package com.br.blog.dto.response;

import com.br.blog.entity.User;

public record LoginResponse(String token, User user) {
}
