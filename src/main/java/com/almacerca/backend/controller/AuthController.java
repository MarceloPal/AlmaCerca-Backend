package com.almacerca.backend.controller;

import com.almacerca.backend.dto.AuthRequest;
import com.almacerca.backend.dto.RegisterRequest;
import com.almacerca.backend.model.User;
import com.almacerca.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
		User u = authService.register(request);
		u.setPassword(null);
		return ResponseEntity.status(HttpStatus.CREATED).body(u);
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody AuthRequest request) {
		User u = authService.login(request);
		u.setPassword(null);
		return ResponseEntity.ok(u);
	}

}

