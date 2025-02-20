package com.fleur.cinemate.usersession;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.usersession.dto.UserSessionDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-sessions")
public class UserSessionController {
    private final UserSessionService userSessionService;

    @GetMapping
    public ResponseEntity<Page<UserSessionDto>> getUserSessions(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(userSessionService.findAllUserSessionByUser(user, pageable));
    }

    @PostMapping("/{userSessionId}")
    public ResponseEntity<Void> deactivateUserSessionById(
            @PathVariable Long userSessionId) {
        userSessionService.deactivateUserSessionById(userSessionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deactivateAllOtherUserSessions(
            @AuthenticationPrincipal User user,
            HttpServletRequest request){
        userSessionService.deactivateAllOtherUserSessions(user, request);
        return ResponseEntity.noContent().build();
    }

}
