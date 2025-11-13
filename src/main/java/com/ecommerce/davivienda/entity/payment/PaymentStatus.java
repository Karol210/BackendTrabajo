package com.ecommerce.davivienda.entity.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa el estado de un pago.
 * Estados posibles: Pendiente, Procesando, Aprobado, Rechazado, Cancelado, Reembolsado, Fallido.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Entity
@Table(name = "estado_pago")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatus {

    /**
     * ID único del estado de pago.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estado_pago_id")
    private Integer paymentStatusId;

    /**
     * Nombre del estado.
     * Ejemplos: "Pendiente", "Aprobado", "Rechazado"
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String name;
}

