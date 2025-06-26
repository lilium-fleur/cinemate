package com.fleur.cinemate.core.person;

import com.fleur.cinemate.core.person.dto.CreatePersonDto;
import com.fleur.cinemate.core.person.dto.PersonDto;
import com.fleur.cinemate.core.person.dto.UpdatePersonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface PersonMapper {

    PersonDto toDto(Person person);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    Person toEntity(CreatePersonDto createPersonDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    void updateEntityFromDto(UpdatePersonDto updatePersonDto, @MappingTarget Person person);
}
