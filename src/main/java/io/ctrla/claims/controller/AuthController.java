package io.ctrla.claims.controller;


import io.ctrla.claims.dto.AuthDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.dto.user.UserDto;
import io.ctrla.claims.entity.User;
import io.ctrla.claims.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody UserDto user) {
        ApiResponse<UserDto> apiResponse = authService.registerNewUser(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<AuthDto>> login(@RequestBody UserDto user) {
        ApiResponse<AuthDto> apiResponse = authService.login(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }
}