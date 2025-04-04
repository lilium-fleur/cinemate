package com.fleur.cinemate.recommendation.userBased.userSimilarity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSimilarityId implements Serializable {
    private Long user1;
    private Long user2;
}
