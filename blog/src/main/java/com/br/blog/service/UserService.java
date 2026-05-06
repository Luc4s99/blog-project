package com.br.blog.service;

import com.br.blog.entity.User;
import com.br.blog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public List<User> getUsers() {

        return  userRepository.findAll();
    }

    public User getUserById(String id) {

        Optional<User> user = userRepository.findById(id);

        return user.orElse(null);
    }

    public User getUserByUsername(String login) {

        return userRepository.getUserByUsername(login);
    }

    public User getUserByEmail(String email) {

        return userRepository.getUserByEmail(email);
    }

    public List<User> getLikeUsername(String loginLike) {

        return userRepository.findLikeLogin(loginLike);
    }

    public User addUser(User user) {

        return userRepository.save(user);
    }

    public User updateUser(User user) {

        return userRepository.save(user);
    }

    public void deleteUserById(String userId) {

        userRepository.deleteById(userId);
    }
}
