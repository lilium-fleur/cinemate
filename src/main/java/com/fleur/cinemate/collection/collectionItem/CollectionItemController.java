package com.fleur.cinemate.collection.collectionItem;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.collection.collectionItem.dto.CreateCollectionItemDto;
import com.fleur.cinemate.collection.collectionItem.dto.CollectionItemDto;
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
@RequestMapping("/api/film-collections/{filmCollectionId}/items")
public class CollectionItemController {
    private final CollectionItemService collectionItemService;

    @PostMapping
    public ResponseEntity<CollectionItemDto> addFilmToCollection(
            @PathVariable Long filmCollectionId,
            @RequestBody @Valid CreateCollectionItemDto createCollectionItemDto,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(collectionItemService.addItemToCollection(createCollectionItemDto, filmCollectionId, user));
    }

    @GetMapping
    public ResponseEntity<Page<CollectionItemDto>> getCollectionItems(
            @PathVariable Long filmCollectionId,
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = "position") Pageable pageable){
        return ResponseEntity.ok(collectionItemService.findItemsByCollection(filmCollectionId, user, pageable));
    }


    @PutMapping("/{itemId}")
    public ResponseEntity<CollectionItemDto> changePositionCollectionItem(
            @PathVariable Long filmCollectionId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user,
            @RequestParam(name = "target_position") Integer targetPosition){
        return ResponseEntity.ok(
                collectionItemService.changePositionCollectionItem(
                        filmCollectionId, user, itemId, targetPosition));
    }


    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeFilmFromCollection(
            @PathVariable Long filmCollectionId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user){
        collectionItemService.removeItemByCollection(filmCollectionId, itemId, user);
        return ResponseEntity.noContent().build();
    }
}
