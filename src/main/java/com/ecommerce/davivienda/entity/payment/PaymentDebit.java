package com.ecommerce.davivienda.entity.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad que representa los detalles específicos de un pago con tarjeta débito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Entity
@Table(name = "pago_debito")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDebit {

    /**
     * ID único del pago débito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pago_debito_id")
    private Integer paymentDebitId;

    /**
     * Pago asociado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id", nullable = false)
    private Payment payment;

    /**
     * Fecha de vencimiento de la tarjeta.
     */
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate expirationDate;

    /**
     * Nombre del titular de la tarjeta.
     */
    @Column(name = "nombre_titular", nullable = false, length = 200)
    private String cardHolderName;

    /**
     * Número de tarjeta (últimos 4 dígitos por seguridad).
     */
    @Column(name = "numero_tarjeta", nullable = false, length = 20)
    private String cardNumber;
}

