package com.ecommerce.davivienda.entity.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa el tipo de pago.
 * Tipos posibles: "debito" o "credito".
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Entity
@Table(name = "tipo_pago")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentType {

    /**
     * Tipo de pago (clave primaria).
     * Valores posibles: "debito", "credito"
     */
    @Id
    @Column(name = "tipo_pago", length = 20)
    private String paymentType;

    /**
     * Nombre descriptivo del tipo de pago.
     * Ejemplos: "Tarjeta Débito", "Tarjeta Crédito"
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String name;
}

