package com.fleur.cinemate.userList;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userList.dto.CreateUserListDto;
import com.fleur.cinemate.userList.dto.UserListDto;
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
@RequestMapping("/api/user-lists")
public class UserListController {
    private final UserListService userListService;

    @PostMapping
    public ResponseEntity<UserListDto> addFilmToList(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateUserListDto createUserListDto){
        return ResponseEntity.ok(userListService.addFilmToList(createUserListDto, user));
    }

    @DeleteMapping("/{userListId}")
    public ResponseEntity<Void> deleteFilmFromList(
            @AuthenticationPrincipal User user,
            @PathVariable Long userListId){
        userListService.removeFilmFromList(userListId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{listType}")
    public ResponseEntity<Page<UserListDto>> getFilmsByList(
            @AuthenticationPrincipal User user,
            @PathVariable String listType,
            @PageableDefault Pageable pageable){
        return ResponseEntity.ok(userListService.findItemsByTypeList(user, listType, pageable));
    }
}
