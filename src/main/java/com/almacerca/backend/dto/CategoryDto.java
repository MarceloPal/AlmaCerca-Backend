package com.almacerca.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar una categoría en la API.
 * Se usa para transferir datos entre cliente y servidor sin exponer
 * directamente la entidad de la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    // Identificador de la categoría (String para compatibilidad con MongoDB ObjectId)
    private String id;

    // Nombre legible de la categoría (ej. "Lácteos")
    private String name;
}
