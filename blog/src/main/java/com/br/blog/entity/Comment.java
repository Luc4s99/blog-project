package com.br.blog.entity;

import java.util.Date;

public class Comment {

    //TODO Adicionar identificadores para comentário

    private String content;
    private Date createdAt;
    private String username;

    public Comment() {
    }

    public Comment(String content, Date createdAt, String username) {
        this.content = content;
        this.createdAt = createdAt;
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
