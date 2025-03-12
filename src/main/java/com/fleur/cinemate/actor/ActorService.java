package com.fleur.cinemate.actor;

import com.fleur.cinemate.actor.dto.ActorDto;
import com.fleur.cinemate.actor.dto.CreateActorDto;
import com.fleur.cinemate.actor.dto.UpdateActorDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ActorService {

    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;


    @Transactional
    public ActorDto createActor(CreateActorDto createActorDto) {

        Actor actor = actorMapper.toEntity(createActorDto);

        return actorMapper.toDto(actorRepository.save(actor));
    }

    @Transactional
    public ActorDto updateActor(UpdateActorDto updateActorDto, Long actorId) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found"));

        actorMapper.updateEntityFromDto(updateActorDto, actor);
        return actorMapper.toDto(actorRepository.save(actor));
    }

    @Transactional(readOnly = true)
    public ActorDto findActorById(Long actorId){
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found"));
        return actorMapper.toDto(actor);
    }

    @Transactional(readOnly = true)
    public Actor findActorEntityById(Long actorId){
        return actorRepository.findById(actorId)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found"));
    }

    @Transactional(readOnly = true)
    public Page<ActorDto> findAllActors(Pageable pageable){
        return actorRepository.findAll(pageable)
                .map(actorMapper::toDto);
    }

    @Transactional
    public void deleteActor(Long actorId){
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found"));

        actorRepository.delete(actor);
    }

}
