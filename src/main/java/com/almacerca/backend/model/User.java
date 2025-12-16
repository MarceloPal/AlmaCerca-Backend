package com.almacerca.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data; // Añadir la importación de Lombok
import lombok.NoArgsConstructor; // Añadir la importación de Lombok
import lombok.AllArgsConstructor; // Añadir la importación de Lombok

@Document(collection = "users")
@Data // Genera automáticamente Getters, Setters, toString, equals y hashCode
@NoArgsConstructor // Genera el constructor vacío
@AllArgsConstructor // Genera el constructor con todos los argumentos (útil para pruebas)
public class User {

	@Id
	private String id;

	private String name;

	private String email;

	private String password;

	private String role;

}