package com.ecommerce.davivienda.security.service.detailsservice;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Interfaz del servicio coordinador de UserDetails para Spring Security.
 * Define el contrato para cargar detalles de usuario desde la base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface JpaUserDetailsService {

    /**
     * Carga un usuario por su email (username).
     * Coordina la búsqueda, validación y construcción del UserDetails.
     *
     * @param email Email del usuario (usado como username)
     * @return UserDetails con información del usuario
     * @throws UsernameNotFoundException si el usuario no existe o no es válido
     */
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
