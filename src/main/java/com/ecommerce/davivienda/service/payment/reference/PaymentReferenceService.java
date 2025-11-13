package com.ecommerce.davivienda.service.payment.reference;

import com.ecommerce.davivienda.entity.payment.PaymentReference;

/**
 * Servicio para generación de referencias únicas de pago.
 * Maneja la lógica de negocio para crear referencias UUID únicas.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentReferenceService {

    /**
     * Genera un número de referencia único para el pago usando UUID.
     * Verifica unicidad en base de datos con reintentos.
     *
     * @return PaymentReference con número único generado y guardado
     */
    PaymentReference generateUniqueReference();
}

