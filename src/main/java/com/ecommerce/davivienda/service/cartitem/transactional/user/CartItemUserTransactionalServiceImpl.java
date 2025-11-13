package com.ecommerce.davivienda.service.cartitem.transactional.user;

import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.repository.user.DocumentTypeRepository;
import com.ecommerce.davivienda.repository.user.UserRepository;
import com.ecommerce.davivienda.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio transaccional para operaciones de User y UserRole.
 * Centraliza el acceso a datos de usuarios, roles y tipos de documento.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemUserTransactionalServiceImpl implements CartItemUserTransactionalService {

    private final UserRoleRepository userRoleRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsUserRoleById(Integer userRoleId) {
        log.debug("Verificando existencia de UserRole con ID: {}", userRoleId);
        return userRoleRepository.existsById(userRoleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRole> findUserRoleById(Integer userRoleId) {
        log.debug("Buscando UserRole con ID: {}", userRoleId);
        return userRoleRepository.findById(userRoleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentType> findDocumentTypeByCodigo(String codigo) {
        log.debug("Buscando tipo de documento con código: {}", codigo);
        return documentTypeRepository.findByCodigo(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByDocumentTypeAndNumber(Integer documentoId, String numeroDeDoc) {
        log.debug("Buscando usuario con documento ID: {} y número: {}", documentoId, numeroDeDoc);
        return userRepository.findByDocumentType_DocumentoIdAndNumeroDeDoc(documentoId, numeroDeDoc);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        log.debug("Buscando usuario con email: {}", email);
        return userRepository.findByCredenciales_Correo(email);
    }
}

