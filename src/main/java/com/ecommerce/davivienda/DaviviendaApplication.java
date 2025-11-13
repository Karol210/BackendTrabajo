package com.ecommerce.davivienda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Aplicación principal de Tienda Digital.
 * Configurada con Clean Architecture y Feign Clients para integraciones externas.
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@SpringBootApplication
@EnableFeignClients
public class DaviviendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaviviendaApplication.class, args);
	}

}
