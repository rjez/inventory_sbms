package com.valcon.inventory.mapper;

import com.valcon.inventory.dto.DocumentDto;
import com.valcon.inventory.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author rjez
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapper {

    @Mapping(target = "byteData", ignore = true)
    DocumentDto toDto(Document doc);
}
