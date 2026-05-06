package com.br.blog.repository;

import com.br.blog.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{'email': ?0}")
    UserDetails findByEmail(String email);

    @Query("{'email': ?0}")
    User getUserByEmail(String email);

    @Query("{'login': ?0}")
    UserDetails findByLogin(String login);

    @Query("{'login': ?0}")
    User getUserByUsername(String login);

    @Query("{'login': {$regex: /?0/}}")
    List<User> findLikeLogin(String loginLike);
}
