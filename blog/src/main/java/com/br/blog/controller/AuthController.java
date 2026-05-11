package com.br.blog.controller;

import com.br.blog.config.TokenConfiguration;
import com.br.blog.dto.request.LoginRequest;
import com.br.blog.dto.request.RefreshTokenRequest;
import com.br.blog.dto.request.RegisterUserRequest;
import com.br.blog.dto.response.LoginResponse;
import com.br.blog.dto.response.RefreshTokenResponse;
import com.br.blog.dto.response.RegisterUserResponse;
import com.br.blog.entity.RefreshToken;
import com.br.blog.entity.User;
import com.br.blog.service.RefreshTokenService;
import com.br.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfiguration tokenConfig;

    public AuthController(UserService userService, RefreshTokenService  refreshTokenService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfiguration tokenConfig) {

        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken userAndPass =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        //Essa autenticação procura por um UserDetailsService para fazer a autenticação
        //Utilizando o metodo loadUserByUsername implementado no AuthService
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();

        if(user != null) {

            //Gerando refresh token
            RefreshToken refreshToken = tokenConfig.generateRefreshToken(user);

            //Gerando access token
            String token = tokenConfig.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(token, refreshToken.getToken(), user));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@RequestBody RegisterUserRequest request) {

        if(this.userService.getUserDetailsByEmail(request.email()) != null) {

            return ResponseEntity.badRequest().build();
        }

        User user = new User();

        user.setLogin(request.login());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userService.addUser(user);

        RefreshToken refreshToken = tokenConfig.generateRefreshToken(user);

        String token = tokenConfig.generateToken(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(savedUser, token, refreshToken.getToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService.getRefreshTokenByToken(request.refreshToken());

        if(refreshToken != null) {

            refreshToken = tokenConfig.verifyExpiration(refreshToken);

            if(refreshToken.isRevoked()) {

                refreshTokenService.deleteRefreshToken(refreshToken);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            //TODO Gerar também novo refresh token

            String accessToken = tokenConfig.generateToken(request.user());

            return ResponseEntity.status(HttpStatus.OK).body(new RefreshTokenResponse(accessToken, refreshToken.getToken()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
