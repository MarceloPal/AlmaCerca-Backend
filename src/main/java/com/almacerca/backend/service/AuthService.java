package com.almacerca.backend.service;

import com.almacerca.backend.dto.AuthRequest;
import com.almacerca.backend.dto.RegisterRequest;
import com.almacerca.backend.exception.ResourceNotFoundException;
import com.almacerca.backend.model.User;
import com.almacerca.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	public User register(RegisterRequest request) {
		Optional<User> existing = userRepository.findByEmail(request.getEmail());
		if (existing.isPresent()) {
			throw new RuntimeException("User already exists");
		}
		User u = new User();
		u.setName(request.getName());
		u.setEmail(request.getEmail());
		// NOTA: Usar BCryptPasswordEncoder aquí es una mejor práctica para producción.
		u.setPassword(request.getPassword()); 
		u.setRole(request.getRole());
		return userRepository.save(u);
	}

	public User login(AuthRequest request) {
		// NOTA: Para producción, se debe usar un decodificador de contraseñas (PasswordEncoder) aquí.
		return userRepository.findByEmail(request.getEmail())
				.filter(u -> u.getPassword().equals(request.getPassword()))
				.orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));
	}

}