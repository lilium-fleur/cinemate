package com.fleur.cinemate.recommendation.userBased.userSimilarity;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserSimilarityService {
    private final UserSimilarityRepository userSimilarityRepository;
    private final UserService userService;


    @Transactional
    public UserSimilarity saveSimilarity(Long user1Id, Long user2Id, Double similarity) {
        if (user1Id > user2Id) {
            long temp = user1Id;
            user1Id = user2Id;
            user2Id = temp;
        }

        User user1 = userService.findById(user1Id);
        User user2 = userService.findById(user2Id);

        UserSimilarity userSimilarity = UserSimilarity.builder()
                .user1(user1)
                .user2(user2)
                .similarity(similarity)
                .build();

        return userSimilarityRepository.save(userSimilarity);
    }

    @Transactional(readOnly = true)
    public List<UserSimilarity> findSimilaritiesByUser(Long userId) {
        return userSimilarityRepository.findByUserId(userId);
    }


    @Transactional(readOnly = true)
    public UserSimilarity findSimilaritiesByUsers(Long user1Id, Long user2Id) {
        if (user1Id > user2Id) {
            long temp = user1Id;
            user1Id = user2Id;
            user2Id = temp;
        }
        return userSimilarityRepository.findByUser1IdAndUser2Id(user1Id, user2Id)
                .orElse(null);
    }

    @Transactional
    public void deleteSimilarityByUser(Long userId) {
        userSimilarityRepository.deleteByUserId(userId);
    }
}
