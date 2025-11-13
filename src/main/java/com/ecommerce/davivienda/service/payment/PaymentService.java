package com.ecommerce.davivienda.service.payment;

import com.ecommerce.davivienda.dto.payment.PaymentProcessRequestDto;
import com.ecommerce.davivienda.dto.payment.PaymentProcessResponseDto;

/**
 * Interfaz de servicio principal para procesamiento de pagos.
 * Define operaciones de alto nivel para procesar pagos con tarjeta.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentService {

    /**
     * Procesa un pago con tarjeta (débito o crédito).
     * 
     * Flujo:
     * 1. Desencripta los datos de la tarjeta
     * 2. Valida el carrito y los datos de la tarjeta
     * 3. Genera número de referencia único
     * 4. Crea registro de pago en estado "Pendiente"
     * 5. Crea registro específico de débito o crédito
     * 
     * @param request Solicitud con cartId y datos encriptados de tarjeta
     * @return PaymentProcessResponseDto con información del pago creado
     */
    PaymentProcessResponseDto processPayment(PaymentProcessRequestDto request);
}

