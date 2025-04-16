package com.fleur.cinemate.core.person;

import com.fleur.cinemate.core.person.dto.CreatePersonDto;
import com.fleur.cinemate.core.person.dto.PersonDto;
import com.fleur.cinemate.core.person.dto.UpdatePersonDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {

    PersonDto toDto(Person person);

    Person toEntity(CreatePersonDto createPersonDto);

    void updateEntityFromDto(UpdatePersonDto updatePersonDto, @MappingTarget Person person);
}
