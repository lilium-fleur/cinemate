package com.fleur.cinemate.user;

import com.fleur.cinemate.collection.CollectionService;
import com.fleur.cinemate.collection.collectionItem.CollectionItemService;
import com.fleur.cinemate.collection.collectionItem.dto.CollectionItemDto;
import com.fleur.cinemate.collection.dto.CollectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final CollectionService collectionService;
    private final CollectionItemService collectionItemService;


    @GetMapping("/{userId}/collections")
    public ResponseEntity<Page<CollectionDto>> findAllFilmCollectionsByUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable){
        return ResponseEntity.ok(collectionService.findCollectionsByUser(user, userId, pageable));
    }

    @GetMapping("/{userId}/collections/{collectionId}")
    public ResponseEntity<Page<CollectionItemDto>> findFilmCollectionItems(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable){
        return ResponseEntity.ok(collectionItemService.findItemsByCollection(collectionId, user, pageable));
    }

}
