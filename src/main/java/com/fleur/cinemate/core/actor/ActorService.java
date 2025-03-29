package com.fleur.cinemate.core.actor;

import com.fleur.cinemate.core.actor.dto.ActorDto;
import com.fleur.cinemate.core.actor.dto.CreateActorDto;
import com.fleur.cinemate.core.actor.dto.UpdateActorDto;
import com.fleur.cinemate.core.relations.filmActor.FilmActor;
import com.fleur.cinemate.core.relations.filmActor.FilmActorRepository;
import com.fleur.cinemate.event.RecordDeletedEvent;
import com.fleur.cinemate.search.repository.ActorDocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class ActorService {

    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;
    private final FilmActorRepository filmActorRepository;
    private final ApplicationEventPublisher eventPublisher;

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
    public Page<ActorDto> findAllActors(Pageable pageable){
        return actorRepository.findAll(pageable)
                .map(actorMapper::toDto);
    }

    @Transactional
    public void deleteActor(Long actorId){
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found"));

        actorRepository.delete(actor);
        eventPublisher.publishEvent(new RecordDeletedEvent(this, actorId, "Actor"));
    }

    @Transactional(readOnly = true)
    public List<ActorDto> findAllActorsById(List<Long> ids){
        return actorRepository.findAllById(ids).stream()
                .map(actorMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<Actor> findAllActorsEntities(Pageable pageable){
        return actorRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Actor> findModifiedSince(Instant since, Pageable pageable){
        return actorRepository.findModifiedSince(since, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ActorDto> findActorsByFilm(Long filmId, Pageable pageable) {
        return filmActorRepository.findByFilmId(filmId, pageable)
                .map(FilmActor::getActor)
                .map(actorMapper::toDto);
    }

}
