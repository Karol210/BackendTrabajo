package com.ecommerce.davivienda.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los tipos de documento de identidad.
 * Mapea la tabla 'documentos' en la base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documentos")
public class DocumentType {

    /**
     * Identificador único del tipo de documento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documento_id")
    private Integer documentoId;

    /**
     * Nombre descriptivo del tipo de documento.
     * Ejemplo: "Cédula de Ciudadanía", "Pasaporte"
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Código abreviado del tipo de documento.
     * Ejemplo: "CC", "PA", "CE"
     */
    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;
}

