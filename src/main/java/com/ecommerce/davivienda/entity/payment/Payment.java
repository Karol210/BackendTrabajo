package com.ecommerce.davivienda.entity.payment;

import com.ecommerce.davivienda.entity.cart.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un pago realizado por un usuario.
 * Contiene información general del pago y relaciones con carrito, tipo de pago, referencia y estado.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Entity
@Table(name = "pago")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    /**
     * ID único del pago.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pago_id")
    private Integer paymentId;

    /**
     * Carrito asociado al pago.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id")
    private Cart cart;

    /**
     * Tipo de pago (débito o crédito).
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_pago_id")
    private PaymentType paymentType;

    /**
     * Fecha y hora del pago.
     */
    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime paymentDate;

    /**
     * Referencia única del pago.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "referencia_id")
    private PaymentReference reference;

    /**
     * Estado actual del pago.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_pago_id")
    private PaymentStatus paymentStatus;

    /**
     * Callback ejecutado antes de persistir la entidad.
     * Establece la fecha de pago si no está definida.
     */
    @PrePersist
    protected void onCreate() {
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
    }
}

