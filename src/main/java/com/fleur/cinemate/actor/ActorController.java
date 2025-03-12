package com.fleur.cinemate.actor;

import com.fleur.cinemate.actor.dto.ActorDto;
import com.fleur.cinemate.actor.dto.CreateActorDto;
import com.fleur.cinemate.actor.dto.UpdateActorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actors")
@RequiredArgsConstructor
public class ActorController {
    private final ActorService actorService;

    @PostMapping
    public ResponseEntity<ActorDto> createActor(
            @RequestBody @Valid CreateActorDto createActorDto){
        return ResponseEntity.ok(actorService.createActor(createActorDto));
    }

    @GetMapping
    public ResponseEntity<Page<ActorDto>> getActors(
            @PageableDefault(sort = "name") Pageable pageable){
        return ResponseEntity.ok(actorService.findAllActors(pageable));
    }

    @GetMapping("/{actorId}")
    public ResponseEntity<ActorDto> getActor(@PathVariable Long actorId){
        return ResponseEntity.ok(actorService.findActorById(actorId));
    }

    @PutMapping("/{actorId}")
    public ResponseEntity<ActorDto> updateActor(
            @PathVariable Long actorId,
            @RequestBody UpdateActorDto updateActorDto){
        return ResponseEntity.ok(actorService.updateActor(updateActorDto, actorId));
    }

    @DeleteMapping("/{actorId}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long actorId){
        actorService.deleteActor(actorId);
        return ResponseEntity.noContent().build();
    }

}
