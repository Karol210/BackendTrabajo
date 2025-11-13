package com.ecommerce.davivienda.entity.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una referencia de pago única.
 * Cada pago tiene una referencia única para trazabilidad.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Entity
@Table(name = "referencias")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReference {

    /**
     * ID único de la referencia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referencia_id")
    private Integer referenceId;

    /**
     * Número de referencia único del pago.
     * Ejemplo: "REF-202511-00001"
     */
    @Column(name = "numero", nullable = false, unique = true, length = 100)
    private String referenceNumber;
}

