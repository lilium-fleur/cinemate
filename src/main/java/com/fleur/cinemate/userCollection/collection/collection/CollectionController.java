package com.fleur.cinemate.userCollection.collection.collection;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userCollection.collection.collection.dto.CreateCollectionDto;
import com.fleur.cinemate.userCollection.collection.collection.dto.CollectionDto;
import com.fleur.cinemate.userCollection.collection.collection.dto.UpdateCollectionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/film-collections")
public class CollectionController {

    private final CollectionService collectionService;


    @PostMapping
    public ResponseEntity<CollectionDto> createCollection(
            @RequestBody @Valid CreateCollectionDto createCollectionDto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(collectionService.createCollection(createCollectionDto, user));
    }


    @GetMapping("/{filmCollectionId}")
    public ResponseEntity<CollectionDto> getCollectionById(
            @PathVariable Long filmCollectionId,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(collectionService.findCollectionById(filmCollectionId, user));
    }

    @PutMapping("/{filmCollectionId}")
    public ResponseEntity<CollectionDto> updateCollection(
            @PathVariable Long filmCollectionId,
            @RequestBody UpdateCollectionDto updateCollectionDto,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(
                collectionService.updateCollection(
                        updateCollectionDto, filmCollectionId, user));
    }

    @DeleteMapping("/{filmCollectionId}")
    public ResponseEntity<CollectionDto> deleteCollection(
            @PathVariable Long filmCollectionId,
            @AuthenticationPrincipal User user){
        collectionService.deleteCollection(filmCollectionId, user);
        return ResponseEntity.noContent().build();
    }



}
