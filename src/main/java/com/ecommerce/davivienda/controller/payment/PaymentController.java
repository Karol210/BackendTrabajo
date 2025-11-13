package com.ecommerce.davivienda.controller.payment;

import com.ecommerce.davivienda.dto.payment.PaymentProcessRequestDto;
import com.ecommerce.davivienda.dto.payment.PaymentProcessResponseDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ecommerce.davivienda.constants.Constants.SUCCESS_PAYMENT_PROCESSED;

/**
 * Controlador REST para operaciones de procesamiento de pagos.
 * Expone endpoints para procesar pagos con tarjeta débito/crédito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Procesa un pago con tarjeta (débito o crédito).
     * 
     * El cartId es OPCIONAL:
     * - Si NO se proporciona: Se usa el carrito activo del usuario autenticado (RECOMENDADO por seguridad)
     * - Si se proporciona: Se valida que exista y tenga items
     * 
     * Los datos de la tarjeta deben venir encriptados en Base64 con el siguiente formato JSON:
     * {
     *   "cardNumber": "1234567812345678",      // Obligatorio
     *   "cardHolderName": "Juan Pérez",        // Obligatorio
     *   "expirationDate": "12/25",             // Opcional (formato MM/YY)
     *   "cvv": "123",                          // Opcional
     *   "installments": 3,                     // Opcional (solo para crédito)
     *   "paymentType": "debito"                // Obligatorio (debito/credito)
     * }
     *
     * @param request Solicitud con cartId (opcional) y datos encriptados de tarjeta
     * @return Response con PaymentProcessResponseDto
     */
    @PostMapping("/process")
    public ResponseEntity<Response<PaymentProcessResponseDto>> processPayment(
            @Valid @RequestBody PaymentProcessRequestDto request) {

        log.info("Solicitud de procesamiento de pago recibida - CartId: {}", 
                request.getCartId() != null ? request.getCartId() : "auto (usuario autenticado)");

        PaymentProcessResponseDto paymentResponse = paymentService.processPayment(request);

        log.info("Pago procesado exitosamente - ID: {}, Referencia: {}",
                paymentResponse.getPaymentId(),
                paymentResponse.getReferenceNumber());

        Response<PaymentProcessResponseDto> response = Response.<PaymentProcessResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_PAYMENT_PROCESSED)
                .body(paymentResponse)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();

        return ResponseEntity.ok(response);
    }
}

