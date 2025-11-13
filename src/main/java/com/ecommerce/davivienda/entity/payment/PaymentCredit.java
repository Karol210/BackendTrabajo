package com.ecommerce.davivienda.entity.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad que representa los detalles específicos de un pago con tarjeta crédito.
 * Incluye número de cuotas adicional respecto al pago débito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Entity
@Table(name = "pago_credito")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCredit {

    /**
     * ID único del pago crédito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pago_credito_id")
    private Integer paymentCreditId;

    /**
     * Pago asociado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id", nullable = false)
    private Payment payment;

    /**
     * Número de cuotas para el pago a crédito.
     * Mínimo 1 cuota.
     */
    @Column(name = "numero_de_cuotas", nullable = false)
    private Integer installments;

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

    /**
     * Fecha de vencimiento de la tarjeta.
     */
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate expirationDate;
}

