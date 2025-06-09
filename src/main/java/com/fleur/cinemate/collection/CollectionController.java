package com.fleur.cinemate.collection;

import com.fleur.cinemate.collection.dto.CollectionDto;
import com.fleur.cinemate.collection.dto.CreateCollectionDto;
import com.fleur.cinemate.collection.dto.UpdateCollectionDto;
import com.fleur.cinemate.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    private final CollectionService collectionService;


    @PostMapping
    public ResponseEntity<CollectionDto> createCollection(
            @RequestBody @Valid CreateCollectionDto createCollectionDto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(collectionService.createCollection(createCollectionDto, user));
    }


    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionDto> getCollectionById(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(collectionService.findCollectionById(collectionId, user));
    }


    @PutMapping("/{collectionId}")
    public ResponseEntity<CollectionDto> updateCollection(
            @PathVariable Long collectionId,
            @RequestBody UpdateCollectionDto updateCollectionDto,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(
                collectionService.updateCollection(
                        updateCollectionDto, collectionId, user));
    }


    @DeleteMapping("/{collectionId}")
    public ResponseEntity<CollectionDto> deleteCollection(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal User user){
        collectionService.deleteCollection(collectionId, user);
        return ResponseEntity.noContent().build();
    }



}
