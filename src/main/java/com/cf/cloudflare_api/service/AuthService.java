package com.cf.cloudflare_api.service;

import com.cf.cloudflare_api.dto.LoginRequest;
import com.cf.cloudflare_api.dto.RegisterRequest;
import com.cf.cloudflare_api.dto.UserResponse;
import com.cf.cloudflare_api.entity.User;
import com.cf.cloudflare_api.util.JwtTokenProvider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Map<String, Object> register(RegisterRequest request) {
        User user = userService.createUser(request);
        String token = jwtTokenProvider.generateToken(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("user", UserResponse.fromUser(user));
        result.put("token", token);

        return result;
    }

    public Map<String, Object> login(LoginRequest request) {
        User user = userService.findByUsernameOrEmail(request.getUsername());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        String token = jwtTokenProvider.generateToken(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("user", UserResponse.fromUser(user));
        result.put("token", token);

        return result;
    }
}
