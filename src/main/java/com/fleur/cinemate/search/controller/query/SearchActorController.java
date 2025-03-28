package com.fleur.cinemate.search.controller.query;

import com.fleur.cinemate.core.actor.dto.ActorDto;
import com.fleur.cinemate.search.service.query.SearchActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/actors")
public class SearchActorController {
    private final SearchActorService searchActorService;

    @GetMapping("/search")
    public ResponseEntity<Page<ActorDto>> searchActors(
            @RequestParam(name = "q") String q,
            @PageableDefault Pageable pageable){
        return ResponseEntity.ok(searchActorService.findActorsByQuery(q, pageable));
    }
}
