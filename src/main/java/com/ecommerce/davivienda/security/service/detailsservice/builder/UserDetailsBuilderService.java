package com.ecommerce.davivienda.security.service.detailsservice.builder;

import com.ecommerce.davivienda.entity.user.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Servicio para construcción de objetos UserDetails de Spring Security.
 * Responsable de transformar entidades User del dominio en UserDetails.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserDetailsBuilderService {

    /**
     * Construye un objeto UserDetails de Spring Security a partir de un usuario.
     *
     * @param user Usuario del dominio
     * @return UserDetails configurado con las autoridades y estado del usuario
     */
    UserDetails buildUserDetails(User user);
}

