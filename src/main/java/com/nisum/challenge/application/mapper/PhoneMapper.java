package com.nisum.challenge.application.mapper;

import com.nisum.challenge.application.dto.PhoneDTO;
import com.nisum.challenge.domain.model.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    @Mapping(target = "id", ignore = true)
    Phone toPhone(PhoneDTO phoneDTO);
}
