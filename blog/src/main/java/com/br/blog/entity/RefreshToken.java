package com.br.blog.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;

@Document(collection = "tokens")
public class RefreshToken {

    @Id
    private String id;
    private String token;
    @Field(targetType = FieldType.OBJECT_ID)
    private String user;
    private Date expiresAt;
    private boolean revoked;

    public RefreshToken(boolean revoked, Date expiresAt, String user, String token) {
        this.revoked = revoked;
        this.expiresAt = expiresAt;
        this.user = user;
        this.token = token;
    }

    public RefreshToken() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
