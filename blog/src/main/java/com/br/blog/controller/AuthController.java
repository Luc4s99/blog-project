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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
        try {

            Authentication authentication = authenticationManager.authenticate(userAndPass);

            User user = (User) authentication.getPrincipal();

            if(user != null) {

                //Gera e salva o refresh token
                RefreshToken refreshToken = refreshTokenService.saveRefreshToken(tokenConfig.generateRefreshToken(user));

                //Armazena o refresh token em um cookie HttpOnly
                ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                        .httpOnly(true)
                        //.secure(true) //Necessita a configuração do HTTPS
                        .path("/api/v1/auth") //Define em quais rotas do backend o cookie será enviado automaticamente pelo navegador
                        .sameSite("Strict")
                        .build();

                //Gerando access token
                String token = tokenConfig.generateToken(user);

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(new LoginResponse(token, user));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (AuthenticationException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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

        //Gera e salva o refresh token
        RefreshToken refreshToken = refreshTokenService.saveRefreshToken(tokenConfig.generateRefreshToken(user));

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                //.secure(true)
                .path("/api/v1/auth")
                .sameSite("Strict")
                .build();

        String token = tokenConfig.generateToken(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new RegisterUserResponse(savedUser, token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request,
                                                             @CookieValue(name = "refreshToken") String refreshToken) {

        if(refreshToken != null) {

            RefreshToken refreshTokenObj = refreshTokenService.getRefreshTokenByToken(refreshToken);

            if(refreshTokenObj != null) {

                refreshTokenObj = tokenConfig.verifyExpiration(refreshTokenObj);

                if(refreshTokenObj.isRevoked()) {

                    refreshTokenService.deleteRefreshToken(refreshTokenObj);

                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                String accessToken = tokenConfig.generateToken(request.user());

                //Gera novo refresh token (token rotation)
                RefreshToken newRefreshToken = refreshTokenService.saveRefreshToken(tokenConfig.generateRefreshToken(request.user()));

                //Apaga refresh token antigo do banco
                refreshTokenService.deleteToken(refreshTokenObj.getToken());

                //Atualiza cookie com o novo refresh token
                ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken.getToken())
                        .httpOnly(true)
                        //.secure(true)
                        .path("/api/v1/auth")
                        .sameSite("Strict")
                        .build();

                return ResponseEntity.status(HttpStatus.OK)
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(new RefreshTokenResponse(accessToken));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken") String refreshToken) {

        if(refreshToken != null) {

            refreshTokenService.deleteToken(refreshToken);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, "")
                    .body("Logout realizado com sucesso");
        }

        return ResponseEntity.badRequest().build();
    }
}
