package com.fleur.cinemate.search.controller.query;

import com.fleur.cinemate.collection.dto.CollectionDto;
import com.fleur.cinemate.search.service.query.SearchCollectionService;
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
@RequestMapping("api/collections")
public class SearchCollectionController {
    private final SearchCollectionService searchCollectionService;

    @GetMapping("/search")
    public ResponseEntity<Page<CollectionDto>> searchCollections(
            @RequestParam(name = "q") String q,
            @PageableDefault Pageable pageable){
        return ResponseEntity.ok(searchCollectionService.findCollectionsByQuery(q, pageable));
    }
}
