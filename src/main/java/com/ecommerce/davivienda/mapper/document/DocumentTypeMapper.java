package com.ecommerce.davivienda.mapper.document;

import com.ecommerce.davivienda.dto.document.DocumentTypeRequestDto;
import com.ecommerce.davivienda.dto.document.DocumentTypeResponseDto;
import com.ecommerce.davivienda.entity.user.DocumentType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para conversiones entre DocumentType y DTOs.
 * MapStruct genera la implementación automáticamente en tiempo de compilación.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface DocumentTypeMapper {

    /**
     * Convierte DocumentTypeRequestDto a entidad DocumentType.
     *
     * @param requestDto DTO con datos de la solicitud
     * @return Entidad DocumentType
     */
    @Mapping(target = "documentoId", ignore = true)
    DocumentType toEntity(DocumentTypeRequestDto requestDto);

    /**
     * Convierte entidad DocumentType a DocumentTypeResponseDto.
     *
     * @param documentType Entidad DocumentType
     * @return DTO de respuesta
     */
    DocumentTypeResponseDto toResponseDto(DocumentType documentType);

    /**
     * Convierte una lista de entidades DocumentType a lista de DTOs de respuesta.
     *
     * @param documentTypes Lista de entidades DocumentType
     * @return Lista de DTOs de respuesta
     */
    List<DocumentTypeResponseDto> toResponseDtoList(List<DocumentType> documentTypes);

    /**
     * Actualiza campos de DocumentType desde DocumentTypeRequestDto.
     *
     * @param requestDto DTO con datos nuevos
     * @param documentType Entidad existente a actualizar
     */
    @Mapping(target = "documentoId", ignore = true)
    void updateEntityFromDto(DocumentTypeRequestDto requestDto, @MappingTarget DocumentType documentType);
}

