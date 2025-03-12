package com.fleur.cinemate.userCollection.collection.filmCollectionItem;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userCollection.collection.filmCollectionItem.dto.CreateFilmCollectionItemDto;
import com.fleur.cinemate.userCollection.collection.filmCollectionItem.dto.FilmCollectionItemDto;
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
public class FilmCollectionItemController {
    private final FilmCollectionItemRepository filmCollectionItemRepository;
    private final FilmCollectionItemService filmCollectionItemService;


    @PostMapping
    public ResponseEntity<FilmCollectionItemDto> addCollectionItem(
            @PathVariable Long filmCollectionId,
            @RequestBody CreateFilmCollectionItemDto createFilmCollectionItemDto,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(filmCollectionItemService.addItemToCollection(createFilmCollectionItemDto, filmCollectionId, user));
    }

    @GetMapping
    public ResponseEntity<Page<FilmCollectionItemDto>> getFilmCollectionItems(
            @PathVariable Long filmCollectionId,
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = "position") Pageable pageable){
        return ResponseEntity.ok(filmCollectionItemService.findItemsByFilmCollection(filmCollectionId, user, pageable));
    }


    @PutMapping("/{itemId}")
    public ResponseEntity<FilmCollectionItemDto> updatePositionCollectionItem(
            @PathVariable Long filmCollectionId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user,
            @RequestParam(name = "target_position") Integer targetPosition){
        return ResponseEntity.ok(
                filmCollectionItemService.changePositionCollectionItem(
                        filmCollectionId, user, itemId, targetPosition));
    }


    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeCollectionItem(
            @PathVariable Long filmCollectionId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user){
        filmCollectionItemService.removeItemByCollection(filmCollectionId, itemId, user);
        return ResponseEntity.noContent().build();
    }
}
