// [EN TU ARCHIVO SecurityConfig.java EN EL BACKEND]

package com.almacerca.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; 
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// Importación necesaria para especificar el método HTTP
import org.springframework.http.HttpMethod; 
// [EN TU ARCHIVO SecurityConfig.java EN EL BACKEND]

// Importación necesaria para especificar el método HTTP
import org.springframework.http.HttpMethod; 

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ... (PasswordEncoder y otros Beans)

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/health").permitAll()
                // Rutas que ya estaban públicas
                .requestMatchers("/api/auth/**", "/api/products/**").permitAll() 
                
                // 🛑 SOLUCIÓN FINAL AL 403 (Permitir TODO el acceso a ADMIN)
                // Permitimos cualquier método HTTP (GET, POST, PUT, DELETE) en /api/admin/products/**
                // Ya que estamos en desarrollo y hemos quitado la lógica requireAdmin()
                .requestMatchers("/api/admin/products/**").permitAll() // ⬅️ CAMBIO CLAVE

                // Rutas de carrito públicas; la validación se hace con el header userId en el controlador
                .requestMatchers("/api/cart/**").permitAll()
                
                // Permitir listado por categorías (Cliente)
                .requestMatchers("/api/products/category/**").permitAll()
                
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }
}