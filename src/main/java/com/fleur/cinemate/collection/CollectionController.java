package com.fleur.cinemate.collection;

import com.fleur.cinemate.collection.dto.CollectionDto;
import com.fleur.cinemate.collection.dto.CollectionDtoWithSize;
import com.fleur.cinemate.collection.dto.CreateCollectionDto;
import com.fleur.cinemate.collection.dto.UpdateCollectionDto;
import com.fleur.cinemate.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping
    public ResponseEntity<Page<CollectionDtoWithSize>> getAllPublicCollection(
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(collectionService.findAllPublicCollection(pageable));
    }


    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionDtoWithSize> getCollectionById(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(collectionService.findCollectionByIdWithSize(collectionId, user));
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
