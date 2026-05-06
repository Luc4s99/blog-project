package com.br.blog.dto.response;

import com.br.blog.entity.Comment;
import com.br.blog.entity.User;

import java.util.Date;
import java.util.List;

public record PostDto(String id, String title, String content, User author, Date createdAt, List<Comment> comments) {
}
