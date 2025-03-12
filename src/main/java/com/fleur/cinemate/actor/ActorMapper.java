package com.fleur.cinemate.actor;

import com.fleur.cinemate.actor.dto.ActorDto;
import com.fleur.cinemate.actor.dto.CreateActorDto;
import com.fleur.cinemate.actor.dto.UpdateActorDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActorMapper {

    ActorDto toDto(Actor actor);

    Actor toEntity(CreateActorDto createActorDto);

    void updateEntityFromDto(UpdateActorDto updateActorDto, @MappingTarget Actor actor);
}
