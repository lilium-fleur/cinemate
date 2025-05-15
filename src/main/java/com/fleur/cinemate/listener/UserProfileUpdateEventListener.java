package com.fleur.cinemate.listener;

import com.fleur.cinemate.event.UserProfileUpdatedEvent;
import com.fleur.cinemate.recommendation.contentBased.UserProfileService;
import com.fleur.cinemate.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Log4j2
public class UserProfileUpdateEventListener {
    private final UserProfileService userProfileService;

    @Async
    @EventListener
    public void handleUserProfileUpdated(UserProfileUpdatedEvent event) {
        try {
            User user = event.getUser();
            userProfileService.buildUserProfile(user);
            log.info("User profile updated");
        } catch (Exception e) {
            log.error("Error while updating user profile", e);
        }
    }
}
