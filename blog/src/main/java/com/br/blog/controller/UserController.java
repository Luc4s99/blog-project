package com.br.blog.controller;

import com.br.blog.entity.User;
import com.br.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @GetMapping(params = "email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {

        User userRetrieved = userService.getUserByEmail(email);

        if(userRetrieved != null) {

            return ResponseEntity.status(HttpStatus.OK).body(userRetrieved);
        }else {

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @GetMapping(params = "username")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {

        User userRetrieved = userService.getUserByUsername(username);

        if(userRetrieved != null) {

            return ResponseEntity.status(HttpStatus.OK).body(userRetrieved);
        }else {

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @GetMapping(params = "usernameLike")
    public ResponseEntity<List<User>> getLikeUsername(@RequestParam String usernameLike) {

        List<User> usersFound = userService.getLikeUsername(usernameLike);

        if(usersFound.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else {

            return ResponseEntity.status(HttpStatus.OK).body(usersFound);
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam String user) {

        userService.deleteUserById(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
