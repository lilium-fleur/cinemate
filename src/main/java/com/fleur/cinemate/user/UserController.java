package com.fleur.cinemate.user;

import com.fleur.cinemate.collection.dto.CollectionDto;
import com.fleur.cinemate.collection.userCollection.UserCollectionService;
import com.fleur.cinemate.collection.userCollection.dto.CreateUserCollectionDto;
import com.fleur.cinemate.collection.userCollection.dto.UserCollectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserCollectionService userCollectionService;

    @GetMapping("/{userId}/collections")
    public ResponseEntity<Page<CollectionDto>> findAllFilmCollectionsByUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal User currentUser,
            @PageableDefault Pageable pageable){
        return ResponseEntity.ok(userCollectionService.getAllCollectionsByUser(userId, currentUser, pageable));
    }

    @PostMapping("/{userId}/collections")
    public ResponseEntity<UserCollectionDto> addCollection(
            @AuthenticationPrincipal User user,
            @RequestBody CreateUserCollectionDto createUserCollectionDto) {
        return ResponseEntity.ok(userCollectionService.addCollection(createUserCollectionDto, user));
    }

    @DeleteMapping("/{userId}/collections/{collectionId}")
    public ResponseEntity<Void> removeCollection(
            @PathVariable Long userId,
            @PathVariable Long collectionId) {
        userCollectionService.removeCollection(collectionId, userId);
        return ResponseEntity.noContent().build();
    }

}
